package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.api.GatewayRateLimitRemoteApi;
import com.github.lyd.base.client.model.GatewayRateLimitApisDto;
import com.github.lyd.base.provider.service.GatewayIpLimitService;
import com.github.lyd.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 网关流量控制
 * @author liuyadu
 */
@Api(tags = "网关流量控制")
@RestController
public class GatewayRateLimitController implements GatewayRateLimitRemoteApi {
    @Autowired
    private GatewayIpLimitService gatewayIpLimitService;

    /**
     * 获取Api限流
     *
     * @return
     */
    @ApiOperation(value = "获取Api限流",notes = "获取Api限流")
    @GetMapping("/gateway/rate/apis")
    @Override
    public ResultBody<List<GatewayRateLimitApisDto>> getRateLimitApiList() {
        return ResultBody.success(gatewayIpLimitService.findWhiteList());
    }
}
