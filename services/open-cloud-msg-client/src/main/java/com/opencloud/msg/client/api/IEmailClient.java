package com.opencloud.msg.client.api;

import com.opencloud.common.model.ResultBody;
import com.opencloud.msg.client.model.EmailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 发送邮件接口
 * @author woodev
 */
public interface IEmailClient {

    /**
     * 邮件通知
     * @return
     */
    @PostMapping("/email")
    ResultBody<String> send(
         @RequestBody EmailMessage message
    );


}
