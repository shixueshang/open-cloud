package com.opencloud.msg.server.controller;

import com.opencloud.common.model.ResultBody;
import com.opencloud.msg.client.api.IEmailClient;
import com.opencloud.msg.client.model.EmailMessage;
import com.opencloud.msg.server.dispatcher.MessageDispatcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woodev
 */
@RestController
@Api(value = "邮件", tags = "邮件")
public class EmailController implements IEmailClient {

    @Autowired
    private MessageDispatcher dispatcher;

    @ApiOperation(value = "发送邮件",notes = "发送邮件")
    @PostMapping("/email")
    @Override
    public ResultBody<String> send(@RequestBody EmailMessage message) {
        this.dispatcher.dispatch(message);
        return ResultBody.ok();
    }



}
