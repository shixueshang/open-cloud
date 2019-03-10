package com.github.lyd.base.client.api;

import com.github.lyd.base.client.model.GatewayIpLimitApisDto;
import com.github.lyd.common.model.ResultBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Ip访问控制
 *
 * @author liuyadu
 */
public interface GatewayIpLimitRemoteApi {
    /**
     * 获取Ip黑名单
     *
     * @return
     */
    @GetMapping("/gateway/ip/blackList")
    ResultBody<List<GatewayIpLimitApisDto>> getBlackList();

    /**
     * 获取IP白名单
     *
     * @return
     */
    @GetMapping("/gateway/ip/whiteList")
    ResultBody<List<GatewayIpLimitApisDto>> getWhiteList();
}
