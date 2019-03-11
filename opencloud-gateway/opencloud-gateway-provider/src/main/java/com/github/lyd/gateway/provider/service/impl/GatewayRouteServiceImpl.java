package com.github.lyd.gateway.provider.service.impl;

import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.gateway.client.model.entity.GatewayRoute;
import com.github.lyd.gateway.provider.mapper.GatewayRouteMapper;
import com.github.lyd.gateway.provider.service.GatewayRouteService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
     * @param keyword
     * @return
     */
    @Override
    public PageList<GatewayRoute> findListPage(PageParams pageParams, String keyword) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        ExampleBuilder builder = new ExampleBuilder(GatewayRoute.class);
        Example example = builder.criteria().end().build();
        List<GatewayRoute> list = gatewayRouteMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询可用路由列表
     *
     * @return
     */
    @Override
    public PageList<GatewayRoute> findRouteList() {
        ExampleBuilder builder = new ExampleBuilder(GatewayRoute.class);
        Example example = builder.criteria().andEqualTo("status", BaseConstants.ENABLED).end().build();
        List<GatewayRoute> list = gatewayRouteMapper.selectByExample(example);
        return new PageList(list);
    }
}
