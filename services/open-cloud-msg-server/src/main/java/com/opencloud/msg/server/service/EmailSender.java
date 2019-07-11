package com.opencloud.msg.server.service;

import com.opencloud.msg.client.model.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * @author woodev
 */
@Slf4j
public class EmailSender {

    private JavaMailSenderImpl javaMailSender;

    private FreeMarkerConfigurer freeMarkerConfigurer;


    /**
     * 发送邮件
     */
    public void sendSimpleMail(EmailMessage emailMessage) {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailMessage.getTo());
            helper.setCc(emailMessage.getCc());
            helper.setFrom(javaMailSender.getUsername());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getContent(), true);
            this.addAttachment(helper, emailMessage);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("发送邮件异常:", e);
        }
    }

    /**
     * 添加附件
     *
     * @param helper
     * @param emailMessage
     * @throws MessagingException
     */
    private void addAttachment(MimeMessageHelper helper, EmailMessage emailMessage) throws MessagingException {
        if (emailMessage.getAttachments() != null && !emailMessage.getAttachments().isEmpty()) {
            List<String> attachments = emailMessage.getAttachments();
            for (String filePath : attachments) {
                File file = new File(filePath);
                helper.addAttachment(file.getName(), file);
            }
        }
    }

    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }

    public void setJavaMailSender(JavaMailSenderImpl javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public FreeMarkerConfigurer getFreeMarkerConfigurer() {
        return freeMarkerConfigurer;
    }

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }


}
