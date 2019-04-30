package com.opencloud.msg.provider.configuration;

import com.opencloud.msg.provider.exchanger.EmailExchanger;
import com.opencloud.msg.provider.exchanger.SmsExchanger;
import com.opencloud.msg.provider.exchanger.WebSocketExchanger;
import com.opencloud.msg.provider.service.SmsSender;
import com.opencloud.msg.provider.service.EmailSender;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author woodev
 */
@Configuration
@AutoConfigureAfter({SmsConfiguration.class, MailConfiguration.class})
public class ExchangerConfiguration {

    @Bean
    public SmsExchanger smsExchanger(SmsSender smsSender){
        return new SmsExchanger(smsSender);
    }

    @Bean
    public EmailExchanger emailExchanger(EmailSender emailSender){
        return new EmailExchanger(emailSender);
    }

    @Bean
    public WebSocketExchanger webSocketExchanger(){
        return new WebSocketExchanger();
    }
}
