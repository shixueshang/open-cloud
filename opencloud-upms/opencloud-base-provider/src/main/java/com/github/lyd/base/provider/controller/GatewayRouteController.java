package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.api.GatewayRouteRemoteApi;
import com.github.lyd.base.client.model.entity.GatewayRoute;
import com.github.lyd.base.provider.service.GatewayRouteService;
import com.github.lyd.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: liuyadu
 * @date: 2018/11/26 18:20
 * @description:
 */
@Api(tags = "网关路由")
@RestController
public class GatewayRouteController implements GatewayRouteRemoteApi {

    @Autowired
    private GatewayRouteService gatewayRouteService;


    /**
     * 获取可用路由列表
     *
     * @return 应用信息
     */
    @ApiOperation(value = "获取可用路由列表", notes = "获取可用路由列表")
    @GetMapping("/gateway/route/list")
    public ResultBody<List<GatewayRoute>> getRouteList() {
        return ResultBody.success(gatewayRouteService.findRouteList());
    }
}
