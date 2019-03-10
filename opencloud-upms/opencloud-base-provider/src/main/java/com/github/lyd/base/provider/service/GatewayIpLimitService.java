package com.github.lyd.base.provider.service;

import com.github.lyd.base.client.model.GatewayIpLimitApisDto;
import com.github.lyd.common.model.PageList;

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
