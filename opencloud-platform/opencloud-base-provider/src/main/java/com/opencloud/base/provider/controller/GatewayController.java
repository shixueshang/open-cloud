package com.opencloud.base.provider.controller;

import com.opencloud.base.client.api.GatewayRemoteApi;
import com.opencloud.base.client.model.GatewayIpLimitApisDto;
import com.opencloud.base.client.model.GatewayRateLimitApisDto;
import com.opencloud.base.client.model.entity.GatewayRoute;
import com.opencloud.base.provider.service.GatewayIpLimitService;
import com.opencloud.base.provider.service.GatewayRateLimitService;
import com.opencloud.base.provider.service.GatewayRouteService;
import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 网关接口
 *
 * @author: liuyadu
 * @date: 2019/3/12 15:12
 * @description:
 */
@Api(tags = "网关对外接口")
@RestController
public class GatewayController implements GatewayRemoteApi {

    @Autowired
    private GatewayIpLimitService gatewayIpLimitService;
    @Autowired
    private GatewayRateLimitService gatewayRateLimitService;
    @Autowired
    private GatewayRouteService gatewayRouteService;
    /**
     * 获取接口黑名单列表
     *
     * @return
     */
    @GetMapping("/gateway/api/blackList")
    @Override
    public ResultBody<List<GatewayIpLimitApisDto>> getApiBlackList() {
        return ResultBody.success(gatewayIpLimitService.findBlackList());
    }

    /**
     * 获取接口白名单列表
     *
     * @return
     */
    @GetMapping("/gateway/api/whiteList")
    @Override
    public ResultBody<List<GatewayIpLimitApisDto>> getApiWhiteList() {
        return ResultBody.success(gatewayIpLimitService.findWhiteList());
    }

    /**
     * 获取限流列表
     *
     * @return
     */
    @GetMapping("/gateway/api/rateLimit")
    @Override
    public ResultBody<List<GatewayRateLimitApisDto>> getApiRateLimitList() {
        return ResultBody.success(gatewayRateLimitService.findRateLimitApiList());
    }

    /**
     * 获取路由列表
     *
     * @return
     */
    @GetMapping("/gateway/api/route")
    @Override
    public ResultBody<List<GatewayRoute>> getApiRouteList() {
        return ResultBody.success(gatewayRouteService.findRouteList());
    }
}
