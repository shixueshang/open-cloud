package com.github.lyd.gateway.provider.service;

import com.github.lyd.common.model.PageList;
import com.github.lyd.gateway.client.model.GatewayIpLimitApisDto;

/**
 * 网关IP访问控制
 */
public interface GatewayIpLimitService {
    /**
     * 查询白名单
     * @return
     */
    PageList<GatewayIpLimitApisDto> findBlackList();

    /**
     * 查询黑名单
     * @return
     */
    PageList<GatewayIpLimitApisDto> findWhiteList();
}
