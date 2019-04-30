package com.opencloud.msg.provider.configuration;

import com.opencloud.common.test.BaseTest;
import com.opencloud.msg.provider.service.DelayMessageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author: liuyadu
 * @date: 2019/2/19 15:23
 * @description:
 */
public class NotifyTest extends BaseTest {
    @Autowired
    private DelayMessageService messageSender;

    @Test
    public void httpNotify() throws Exception {
        messageSender.httpNotify("http://www.baidu.com/notity/callback", "order_pay", new HashMap<>());
        System.out.println("发送成功");
        Thread.sleep(500000);
    }
}
