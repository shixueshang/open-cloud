package com.github.lyd.base.provider.service;

import com.github.lyd.base.client.model.entity.GatewayRoute;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;

/**
 * 访问日志
 */
public interface GatewayRouteService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @param keyword
     * @return
     */
    PageList<GatewayRoute> findListPage(PageParams pageParams, String keyword);

    /**
     * 查询可用路由列表
     *
     * @return
     */
    PageList<GatewayRoute> findRouteList();
}
