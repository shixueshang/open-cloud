package com.github.lyd.gateway.provider.service;

import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.gateway.client.model.entity.GatewayAccessLogs;

import java.util.Map;

/**
 * 网关访问日志
 */
public interface GatewayAccessLogsService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @param keyword
     * @return
     */
    PageList<GatewayAccessLogs> findListPage(PageParams pageParams, String keyword);

    /**
     * 保存日志
     * @param map
     */
    void  saveLogs(Map map);
}
