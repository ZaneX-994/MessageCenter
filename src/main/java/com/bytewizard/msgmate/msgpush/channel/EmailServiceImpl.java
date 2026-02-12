package com.bytewizard.msgmate.msgpush.channel;

import com.bytewizard.msgmate.common.conf.SendMsgConf;
import com.bytewizard.msgmate.msgpush.MsgPushService;
import com.bytewizard.msgmate.msgpush.base.ChannelMsgBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements MsgPushService {

    @Autowired
    private SendMsgConf sendMsgConf;

    @Override
    public void pushMsg(ChannelMsgBase msgBase) {
        log.info("发送 Email !!!!! content:"+msgBase.getContent());

        // SMTP服务器配置
        String host = sendMsgConf.getEmailHost(); // SMTP服务器地址
        String port = sendMsgConf.getEmailPort(); // SMTP端口（通常为587或465）
        String username = sendMsgConf.getEmailAccount(); // 发件人邮箱
        String password = sendMsgConf.getEmailAuthCode(); // 发件人邮箱密码或授权码
    }
}
