package com.opencloud.msg.server.exchanger;


import com.opencloud.msg.client.model.EmailMessage;
import com.opencloud.msg.client.model.BaseMessage;
import com.opencloud.msg.server.service.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @author woodev
 */
@Slf4j
public class EmailExchanger implements MessageExchanger {

    private EmailSender mailSender;
    public EmailExchanger(EmailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean support(Object notification) {
        return notification.getClass().equals(EmailMessage.class);
    }

    @Override
    public boolean exchange(BaseMessage notify) {
        Assert.notNull(mailSender, "邮件接口没有初始化");
        EmailMessage mailMessage = (EmailMessage) notify;
        mailSender.sendSimpleMail(mailMessage);
        return true;
    }
}
