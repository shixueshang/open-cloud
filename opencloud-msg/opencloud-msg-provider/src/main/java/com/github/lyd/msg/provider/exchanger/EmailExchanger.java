package com.github.lyd.msg.provider.exchanger;


import com.github.lyd.msg.client.model.Notify;
import com.github.lyd.msg.client.model.EmailNotify;
import com.github.lyd.msg.provider.locator.MailSenderLocator;
import com.github.lyd.msg.provider.service.impl.MailSenderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.Assert;

import java.util.Random;

/**
 * @author woodev
 */
@Slf4j
public class EmailExchanger implements MessageExchanger {

    private MailSenderImpl mailSender;
    private MailSenderLocator mailSenderLocator;
    public EmailExchanger(MailSenderImpl mailSender, MailSenderLocator mailSenderLocator) {
        this.mailSender = mailSender;
        this.mailSenderLocator = mailSenderLocator;
    }

    @Override
    public boolean support(Object notification) {
        return notification.getClass().equals(EmailNotify.class);
    }

    @Override
    public boolean exchange(Notify notify) {
        Assert.notNull(mailSender, "邮件接口没有初始化");
        mailSender.setJavaMailSender(getJavaMailSender());
        EmailNotify mailMessage = (EmailNotify) notify;
        mailSender.sendSimpleMail(mailMessage);
        return true;
    }

    public JavaMailSenderImpl getJavaMailSender() {
        String[] keys = mailSenderLocator.getMailSenders().keySet().toArray(new String[0]);
        Random random = new Random();
        // 随机获取邮箱发送者
        String randomKey = keys[random.nextInt(keys.length)];
        log.info("Email sender {}",randomKey);
        return  mailSenderLocator.getMailSenders().get(randomKey);
    }
}
