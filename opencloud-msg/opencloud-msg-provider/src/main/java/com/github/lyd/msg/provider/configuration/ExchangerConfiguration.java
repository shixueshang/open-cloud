package com.github.lyd.msg.provider.configuration;

import com.github.lyd.msg.provider.exchanger.EmailExchanger;
import com.github.lyd.msg.provider.exchanger.SmsExchanger;
import com.github.lyd.msg.provider.exchanger.WebSocketExchanger;
import com.github.lyd.msg.provider.locator.MailSenderLocator;
import com.github.lyd.msg.provider.service.SmsSender;
import com.github.lyd.msg.provider.service.impl.MailSenderImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author woodev
 */
@Configuration
@EnableAutoConfiguration
@AutoConfigureAfter({SmsConfiguration.class, MailConfiguration.class})
public class ExchangerConfiguration {

    @Bean
    public SmsExchanger smsNotifcationExchanger(SmsSender smsSender){
        return new SmsExchanger(smsSender);
    }

    @Bean
    public EmailExchanger emailNoficationExchanger(MailSenderImpl mailSender, MailSenderLocator mailSenderLocator){
        return new EmailExchanger(mailSender,mailSenderLocator);
    }

    @Bean
    public WebSocketExchanger webSocketNotificationExchanger(){
        return new WebSocketExchanger();
    }
}
