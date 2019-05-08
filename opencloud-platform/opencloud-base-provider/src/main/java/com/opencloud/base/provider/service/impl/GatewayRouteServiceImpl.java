package com.opencloud.base.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.entity.GatewayRoute;
import com.opencloud.base.provider.mapper.GatewayRouteMapper;
import com.opencloud.base.provider.service.GatewayRouteService;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GatewayRouteServiceImpl implements GatewayRouteService {

    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<GatewayRoute> findListPage(PageParams pageParams) {
        QueryWrapper<GatewayRoute> queryWrapper = new QueryWrapper();
        return gatewayRouteMapper.selectPage(new Page(pageParams.getPage(),pageParams.getLimit()),queryWrapper);
    }

    /**
     * 查询可用路由列表
     *
     * @return
     */
    @Override
    public List<GatewayRoute> findRouteList() {
        QueryWrapper<GatewayRoute> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GatewayRoute::getStatus,BaseConstants.ENABLED);
        List<GatewayRoute> list = gatewayRouteMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 获取路由信息
     *
     * @param routeId
     * @return
     */
    @Override
    public GatewayRoute getRoute(Long routeId) {
        return gatewayRouteMapper.selectById(routeId);
    }

    /**
     * 添加路由
     *
     * @param route
     */
    @Override
    public GatewayRoute addRoute(GatewayRoute route) {
        if (StringUtils.isBlank(route.getPath())) {
            throw new OpenAlertException(String.format("path不能为空!"));
        }
        if (isExist(route.getPath())) {
            throw new OpenAlertException(String.format("path已存在!"));
        }
        route.setIsPersist(0);
        gatewayRouteMapper.insert(route);
        return route;
    }

    /**
     * 更新路由
     *
     * @param route
     */
    @Override
    public GatewayRoute updateRoute(GatewayRoute route) {
        if (StringUtils.isBlank(route.getPath())) {
            throw new OpenAlertException(String.format("path不能为空"));
        }
        GatewayRoute saved = getRoute(route.getRouteId());
        if (saved == null) {
            throw new OpenAlertException("路由信息不存在!");
        }
        if (saved != null && saved.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许修改"));
        }
        if (!saved.getPath().equals(route.getPath())) {
            // 和原来不一致重新检查唯一性
            if (isExist(route.getPath())) {
                throw new OpenAlertException("path已存在!");
            }
        }
        gatewayRouteMapper.updateById(route);
        return route;
    }

    /**
     * 删除路由
     *
     * @param routeId
     */
    @Override
    public void removeRoute(Long routeId) {
        GatewayRoute saved = getRoute(routeId);
        if (saved != null && saved.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        gatewayRouteMapper.deleteById(routeId);
    }

    /**
     * 查询地址是否存在
     *
     * @param path
     */
    @Override
    public Boolean isExist(String path) {
        QueryWrapper<GatewayRoute> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GatewayRoute::getPath,path);
        int count = gatewayRouteMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
