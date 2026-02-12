package com.bytewizard.msgmate.mamager;

import com.bytewizard.msgmate.model.dto.SendMsgReq;

public interface SendMsgManager {
    public String SendToMysql(SendMsgReq sendMsgReq);
    public String SendToMq(SendMsgReq sendMsgReq);
    public String SendToTimer(SendMsgReq sendMsgReq);
}
