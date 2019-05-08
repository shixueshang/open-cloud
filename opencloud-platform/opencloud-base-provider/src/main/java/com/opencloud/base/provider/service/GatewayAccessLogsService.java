package com.opencloud.base.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.GatewayAccessLogs;
import com.opencloud.common.model.PageParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 网关访问日志
 * @author liuyadu
 */
public interface GatewayAccessLogsService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<GatewayAccessLogs> findListPage(PageParams pageParams);
}
