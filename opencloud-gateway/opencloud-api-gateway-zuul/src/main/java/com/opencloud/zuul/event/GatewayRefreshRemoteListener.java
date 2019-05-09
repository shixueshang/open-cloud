package com.opencloud.zuul.event;

import com.opencloud.zuul.locator.DbRouteLocator;
import com.opencloud.zuul.locator.AccessLocator;
import org.springframework.context.ApplicationListener;

/**
 * 自定义动态限流刷新事件监听
 *
 * @author liuyadu
 */
public class GatewayRefreshRemoteListener implements ApplicationListener<GatewayRefreshRemoteApplicationEvent> {

    /**
     * 路由加载器
     */
    private DbRouteLocator routesLocator;

    /**
     * 权限加载器
     */
    private AccessLocator permissionLocator;

    public GatewayRefreshRemoteListener(DbRouteLocator routesLocator, AccessLocator permissionLocator) {
        this.routesLocator = routesLocator;
        this.permissionLocator = permissionLocator;
    }


    public DbRouteLocator getRoutesLocator() {
        return routesLocator;
    }

    public void setRoutesLocator(DbRouteLocator routesLocator) {
        this.routesLocator = routesLocator;
    }

    @Override
    public void onApplicationEvent(GatewayRefreshRemoteApplicationEvent event) {
        //重载路由
        routesLocator.doRefresh();
        //重载权限
        permissionLocator.doRefresh();
    }
}
