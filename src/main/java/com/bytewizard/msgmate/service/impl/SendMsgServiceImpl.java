package com.bytewizard.msgmate.service.impl;

import com.bytewizard.msgmate.common.conf.SendMsgConf;
import com.bytewizard.msgmate.mamager.SendMsgManager;
import com.bytewizard.msgmate.model.dto.SendMsgReq;
import com.bytewizard.msgmate.service.SendMsgService;
import com.bytewizard.msgmate.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendMsgServiceImpl implements SendMsgService {

    @Autowired
    private TemplateService templateService;

    @Autowired
    SendMsgConf sendMsgConf;

    @Autowired
    SendMsgManager sendMsgManager;

    @Override
    public String SendMsg(SendMsgReq sendMsgReq) {
        return "";
    }
}
