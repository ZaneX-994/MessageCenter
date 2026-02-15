package com.bytewizard.msgmate.mamager;

import com.bytewizard.msgmate.constant.Constants;
import com.bytewizard.msgmate.enums.MsgStatus;
import com.bytewizard.msgmate.enums.PriorityEnum;
import com.bytewizard.msgmate.mapper.MsgQueueMapper;
import com.bytewizard.msgmate.model.MsgQueueModel;
import com.bytewizard.msgmate.model.dto.SendMsgReq;
import com.bytewizard.msgmate.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class SendMsgManagerImpl implements SendMsgManager {

    @Autowired
    MsgQueueMapper msgQueueMapper;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public String SendToMysql(SendMsgReq sendMsgReq) {
        // 1. 生成消息 Id; msgId
        MsgQueueModel newMsgModel = new MsgQueueModel();
        if(StringUtils.isEmpty(sendMsgReq.getMsgID())) {
            sendMsgReq.setMsgID(UUID.randomUUID().toString());
        }

        // 2.构建 Mysql 中转消息模型 MsgQueueModel
        newMsgModel.setMsgId(sendMsgReq.getMsgID());
        newMsgModel.setSubject(sendMsgReq.getSubject());
        newMsgModel.setTo(sendMsgReq.getTo());
        newMsgModel.setPriority(sendMsgReq.getPriority());
        newMsgModel.setTemplateId(sendMsgReq.getTemplateId());
        newMsgModel.setTemplateData(JSONUtil.toJsonString(sendMsgReq.getTemplateData()));
        newMsgModel.setStatus(MsgStatus.Pending.getStatus());

        // 3.根据优先级确定要存入的表的表名    low|middle|high|retry
        String tableName = Constants.TableNamePre_MsgQueue+ PriorityEnum.GetPriorityStr(sendMsgReq.getPriority());

        // 4.存入数据库
        try {
            msgQueueMapper.save(tableName,newMsgModel);
        } catch (Exception e){
            log.error("存储优先级消息失败 msgid:"+newMsgModel.getMsgId());
        }

        // 返回消息 id
        return  sendMsgReq.getMsgID();
    }

    @Override
    public String SendToMq(SendMsgReq sendMsgReq) {

        // 1. 生成 MsgID
        if(StringUtils.isEmpty(sendMsgReq.getMsgID())) {
            sendMsgReq.setMsgID(UUID.randomUUID().toString());
        }

        // 2. 序列化请求为 一条 String 消息
        String mqData = JSONUtil.toJsonString(sendMsgReq);

        // 3.根据消息优先级，确定要投递的 Topic    low-topic|middel-topic|high-topic
        String topic = PriorityEnum.GetPriorityStr(sendMsgReq.getPriority()) + Constants.Topic_Tail_MsgQueue;

        //4. 发送消息到消息队列中转
        kafkaTemplate.send(topic, mqData);

        // 5. 返回消息Id
        return  sendMsgReq.getMsgID();
    }

    @Override
    public String SendToTimer(SendMsgReq sendMsgReq) {
        return "";
    }
}
