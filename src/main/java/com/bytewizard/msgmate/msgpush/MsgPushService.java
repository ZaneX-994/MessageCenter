package com.bytewizard.msgmate.msgpush;


import com.bytewizard.msgmate.msgpush.base.ChannelMsgBase;

public interface MsgPushService {
    void pushMsg(ChannelMsgBase msgBase);
}
