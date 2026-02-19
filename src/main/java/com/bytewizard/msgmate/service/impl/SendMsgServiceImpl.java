package com.bytewizard.msgmate.service.impl;

import com.bytewizard.msgmate.common.conf.SendMsgConf;
import com.bytewizard.msgmate.enums.MsgStatus;
import com.bytewizard.msgmate.enums.TemplateStatus;
import com.bytewizard.msgmate.exception.BusinessException;
import com.bytewizard.msgmate.exception.ErrorCode;
import com.bytewizard.msgmate.mamager.SendMsgManager;
import com.bytewizard.msgmate.model.TemplateModel;
import com.bytewizard.msgmate.model.dto.SendMsgReq;
import com.bytewizard.msgmate.service.SendMsgService;
import com.bytewizard.msgmate.service.TemplateService;
import com.bytewizard.msgmate.tools.MsgRecordService;
import com.bytewizard.msgmate.tools.RateLimitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendMsgServiceImpl implements SendMsgService {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private SendMsgConf sendMsgConf;

    @Autowired
    private SendMsgManager sendMsgManager;

    @Autowired
    private MsgRecordService msgRecordService;

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public String SendMsg(SendMsgReq sendMsgReq) {

        // 1.校验发送参数（略)
        if (StringUtils.isEmpty(sendMsgReq.getTo())
            || StringUtils.isEmpty(sendMsgReq.getSubject())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "检查参数是否正确");
        }

        // 2.查询模板&校验模板状态
        TemplateModel tp = templateService.GetTemplateWithCache(sendMsgReq.getTemplateId());
        if (tp.getStatus() != TemplateStatus.TEMPLATE_STATUS_NORMAL.getStatus()){
            throw new BusinessException(ErrorCode.TEMPLATE_STATUS_ERROR, "模板尚未准备好，检查模板状态");
        }

        // 判断是否为定时消息
        boolean isTimerMsg = sendMsgReq.getSendTimestamp() != null;

        // 3.校验发送配额
        boolean allowed = rateLimitService.isRequestAllowed(tp.getSourceId(), tp.getChannel(), isTimerMsg);
        if(!allowed){
            log.warn("请求频繁，限流了，请稍后重试");
            throw new BusinessException(ErrorCode.RateLimit_ERROR,"请求频繁，限流了，请稍后重试");
        }

        // 4.发送到缓冲区 定时｜Mysql 缓冲｜MQ 缓冲
        if (isTimerMsg){
            return sendMsgManager.SendToTimer(sendMsgReq);
        }

        String msgId = null;
        if (sendMsgConf.isMysqlAsMq()){
            // 发送到 Mysql
            msgId = sendMsgManager.SendToMysql(sendMsgReq);
        } else {
            // 发送到 MQ
            msgId = sendMsgManager.SendToMq(sendMsgReq);
        }

        if (!StringUtils.isEmpty(msgId)){
            // 记录消息记录
            msgRecordService.CreateMsgRecord(msgId,sendMsgReq,tp, MsgStatus.Pending);
        }

        return msgId;

    }
}
