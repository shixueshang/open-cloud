package com.github.lyd.msg.provider.controller;

import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.msg.client.api.HttpNotifyRemoteApi;
import com.github.lyd.msg.client.model.HttpNotify;
import com.github.lyd.msg.client.model.entity.NotifyHttpLogs;
import com.github.lyd.msg.provider.service.DelayMessageService;
import com.github.lyd.msg.provider.service.NotifyHttpLogsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author woodev
 */
@RestController
@Api(value = "异步通知", tags = "异步通知")
public class HttpNotifyController implements HttpNotifyRemoteApi {

    @Autowired
    private DelayMessageService delayMessageService;
    @Autowired
    private NotifyHttpLogsService notifyHttpLogsService;

    @ApiOperation(value = "发送HTTP异步通知",notes = "发送HTTP异步通知")
    @PostMapping("/http/notify")
    @Override
    public ResultBody<String> sendHttpNotify(
           @Valid @RequestBody HttpNotify httpNotify
    ) {
        try {
            delayMessageService.httpNotify(httpNotify.getUrl(), httpNotify.getType(), httpNotify.getData());
            return ResultBody.success("");
        } catch (Exception e) {
            return ResultBody.failed(e.getMessage());
        }
    }


    /**
     * 获取分页异步通知列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页异步通知列表", notes = "获取分页异步通知列表")
    @GetMapping("/http/notify/logs")
    public ResultBody<PageList<NotifyHttpLogs>> getNotifyHttpLogListPage(@RequestParam Map map) {
        return ResultBody.success(notifyHttpLogsService.findListPage(new PageParams(map)));
    }

}
