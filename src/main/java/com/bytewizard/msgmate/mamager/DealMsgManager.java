package com.bytewizard.msgmate.mamager;

import com.bytewizard.msgmate.model.dto.SendMsgReq;

public interface DealMsgManager {
    public void DealOneMsg(SendMsgReq sendMsgReq);
}
