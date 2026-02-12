package com.bytewizard.msgmate;

import com.bytewizard.msgmate.common.conf.SendMsgConf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@SpringBootTest
class MsgMateApplicationTests {
    @Autowired
    private SendMsgConf sendMsgConf;

    @Test
    void contextLoads() {
    }

    @Test
    void emailSendTest(){
        // SMTP服务器配置
        String host = sendMsgConf.getEmailHost(); // SMTP服务器地址
        String port = sendMsgConf.getEmailPort(); // SMTP端口（通常为587或465）
        String username = sendMsgConf.getEmailAccount(); // 发件人邮箱
        String password = sendMsgConf.getEmailAuthCode(); // 发件人邮箱密码或授权码

        String recipient = "201668468@qq.com";

        String subject = "测试邮件";
        String body = "这是一封测试邮件, 来自Java程序";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("邮件发送失败");
        }

    }

}
