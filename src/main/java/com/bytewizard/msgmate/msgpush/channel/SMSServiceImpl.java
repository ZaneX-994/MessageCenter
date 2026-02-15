package com.bytewizard.msgmate.msgpush.channel;

import com.bytewizard.msgmate.msgpush.MsgPushService;
import com.bytewizard.msgmate.msgpush.base.ChannelMsgBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SMSServiceImpl implements MsgPushService {

    @Override
    public void pushMsg(ChannelMsgBase msgBase) {

        log.info("发送 SMS 短信!!!!! content:"+msgBase.getContent());
    }
}
