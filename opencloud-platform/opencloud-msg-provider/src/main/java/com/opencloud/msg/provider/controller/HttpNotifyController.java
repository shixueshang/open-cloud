package com.opencloud.msg.provider.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.utils.StringUtils;
import com.opencloud.msg.client.api.HttpNotifyRemoteApi;
import com.opencloud.msg.client.model.HttpNotify;
import com.opencloud.msg.client.model.entity.NotifyHttpLogs;
import com.opencloud.msg.provider.service.DelayMessageService;
import com.opencloud.msg.provider.service.NotifyHttpLogsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "发送HTTP异步通知", notes = "发送HTTP异步通知")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "通知地址", required = true, paramType = "form"),
            @ApiImplicitParam(name = "type", value = "通知业务类型", required = true, paramType = "form"),
            @ApiImplicitParam(name = "params", value = "通知参数:json字符串", required = false, paramType = "form"),
    })
    @PostMapping("/http/notify")
    @Override
    public ResultBody<String> sendHttpNotify(
            @RequestParam(value = "url", required = true) String url,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "params", required = false) String params
    ) throws Exception {
        HttpNotify httpNotify = new HttpNotify();
        httpNotify.setUrl(url);
        httpNotify.setType(type);
        try {
            if (StringUtils.isNotBlank(params)) {
                httpNotify.setData(JSONObject.parseObject(params, Map.class));
            }
        } catch (Exception e) {
        }
        delayMessageService.httpNotify(httpNotify);
        return ResultBody.ok();

    }


    /**
     * 获取分页异步通知列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页异步通知列表", notes = "获取分页异步通知列表")
    @GetMapping("/http/notify/logs")
    public ResultBody<IPage<NotifyHttpLogs>> getNotifyHttpLogListPage(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(notifyHttpLogsService.findListPage(new PageParams(map)));
    }

}
