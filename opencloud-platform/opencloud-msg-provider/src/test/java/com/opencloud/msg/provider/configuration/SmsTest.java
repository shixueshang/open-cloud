package com.opencloud.msg.provider.configuration;

import com.opencloud.common.test.BaseTest;
import com.opencloud.msg.client.model.SmsNotify;
import com.opencloud.msg.provider.dispatcher.MessageDispatcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: liuyadu
 * @date: 2018/11/27 14:45
 * @description:
 */
public class SmsTest extends BaseTest {
    @Autowired
    private MessageDispatcher dispatcher;

    @Test
    public void testSms() {
        SmsNotify smsNotify = new SmsNotify();
        smsNotify.setPhoneNumber("18510152531");
        smsNotify.setSignName("测试");
        smsNotify.setTemplateCode("测试内容");
        this.dispatcher.dispatch(smsNotify);
        try {
            Thread.sleep(50000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
