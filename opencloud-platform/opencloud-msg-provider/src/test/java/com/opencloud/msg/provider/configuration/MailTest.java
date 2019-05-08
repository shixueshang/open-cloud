package com.opencloud.msg.provider.configuration;

import com.opencloud.common.test.BaseTest;
import com.opencloud.msg.client.model.EmailNotify;
import com.opencloud.msg.provider.dispatcher.MessageDispatcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: liuyadu
 * @date: 2018/11/27 14:45
 * @description:
 */
public class MailTest extends BaseTest {
    @Autowired
    private MessageDispatcher dispatcher;

    @Test
    public void testMail() {
        EmailNotify message = new EmailNotify();
        message.setTo("515608851@qq.com");
        message.setTitle("测试");
        message.setContent("测试内容");
        this.dispatcher.dispatch(message);
        try {
            Thread.sleep(50000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
