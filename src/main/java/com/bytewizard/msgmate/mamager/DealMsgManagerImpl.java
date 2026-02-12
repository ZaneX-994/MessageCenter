package com.bytewizard.msgmate.mamager;

import com.bytewizard.msgmate.model.dto.SendMsgReq;
import com.bytewizard.msgmate.msgpush.MsgPushService;

import java.util.HashMap;
import java.util.Map;

public class DealMsgManagerImpl implements DealMsgManager {

    public static Map<Integer, MsgPushService> channelStrategyMap = new HashMap<>();

    @Override
    public void DealOneMsg(SendMsgReq sendMsgReq) {

    }
}
