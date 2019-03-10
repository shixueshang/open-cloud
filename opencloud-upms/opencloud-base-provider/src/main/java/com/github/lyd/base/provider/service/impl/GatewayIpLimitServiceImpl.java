package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.model.GatewayIpLimitApisDto;
import com.github.lyd.base.provider.mapper.GatewayIpLimitApisMapper;
import com.github.lyd.base.provider.service.GatewayIpLimitService;
import com.github.lyd.common.model.PageList;
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
public class GatewayIpLimitServiceImpl implements GatewayIpLimitService {

    @Autowired
    private GatewayIpLimitApisMapper gatewayIpLimitApisMapper;

    /**
     * 查询白名单
     *
     * @return
     */
    @Override
    public PageList<GatewayIpLimitApisDto> findBlackList() {
        List<GatewayIpLimitApisDto> list = gatewayIpLimitApisMapper.selectIpLimitApisDto(0);
        return new PageList<>(list);
    }

    /**
     * 查询黑名单
     *
     * @return
     */
    @Override
    public PageList<GatewayIpLimitApisDto> findWhiteList() {
        List<GatewayIpLimitApisDto> list = gatewayIpLimitApisMapper.selectIpLimitApisDto(1);
        return new PageList<>(list);
    }
}
