package com.opencloud.base.client.api;

import com.opencloud.base.client.model.GatewayIpLimitApisDto;
import com.opencloud.base.client.model.GatewayRateLimitApisDto;
import com.opencloud.base.client.model.entity.GatewayRoute;
import com.opencloud.common.model.ResultBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author liuyadu
 */
public interface GatewayRemoteApi {

    /**
     * 获取接口黑名单列表
     *
     * @return
     */
    @GetMapping("/gateway/api/blackList")
     ResultBody<List<GatewayIpLimitApisDto>> getApiBlackList() ;

    /**
     * 获取接口白名单列表
     * @return
     */
    @GetMapping("/gateway/api/whiteList")
    ResultBody<List<GatewayIpLimitApisDto> > getApiWhiteList();

    /**
     * 获取限流列表
     * @return
     */
    @GetMapping("/gateway/api/rateLimit")
    ResultBody<List<GatewayRateLimitApisDto> > getApiRateLimitList();

    /**
     * 获取路由列表
     * @return
     */
    @GetMapping("/gateway/api/route")
    ResultBody<List<GatewayRoute> > getApiRouteList();
}
