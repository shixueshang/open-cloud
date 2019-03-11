package com.github.lyd.gateway.provider.service;

import com.github.lyd.common.model.PageList;
import com.github.lyd.gateway.client.model.GatewayRateLimitApisDto;

/**
 * 访问日志
 * @author liuyadu
 */
public interface GatewayRateLimitService {
    /**
     * 查询接口流量限制
     *
     * @return
     */
    PageList<GatewayRateLimitApisDto> findRateLimitApiList();
}
