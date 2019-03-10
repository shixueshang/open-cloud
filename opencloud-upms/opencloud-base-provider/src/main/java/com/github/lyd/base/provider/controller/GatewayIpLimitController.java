package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.model.GatewayIpLimitApisDto;
import com.github.lyd.base.client.api.GatewayIpLimitRemoteApi;
import com.github.lyd.base.provider.service.GatewayIpLimitService;
import com.github.lyd.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 网关Ip访问控制
 * @author liuyadu
 */
@Api(tags = "网关Ip访问控制")
@RestController
public class GatewayIpLimitController implements GatewayIpLimitRemoteApi {
    @Autowired
    private GatewayIpLimitService gatewayIpLimitService;


    /**
     * 获取Ip黑名单
     *
     * @return
     */
    @ApiOperation(value = "获取Ip黑名单",notes = "仅限系统内部调用")
    @GetMapping("/gateway/ip/blackList")
    @Override
    public ResultBody<List<GatewayIpLimitApisDto>> getBlackList() {
        return ResultBody.success(gatewayIpLimitService.findBlackList());
    }

    /**
     * 获取IP白名单
     *
     * @return
     */
    @ApiOperation(value = "获取IP白名单",notes = "仅限系统内部调用")
    @GetMapping("/gateway/ip/whiteList")
    @Override
    public ResultBody<List<GatewayIpLimitApisDto>> getWhiteList() {
        return ResultBody.success(gatewayIpLimitService.findWhiteList());
    }
}
