package com.github.lyd.gateway.provider.service.impl;

import com.github.lyd.common.model.PageList;
import com.github.lyd.gateway.client.model.GatewayRateLimitApisDto;
import com.github.lyd.gateway.provider.mapper.GatewayRateLimitApisMapper;
import com.github.lyd.gateway.provider.service.GatewayRateLimitService;
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
public class GatewayRateLimitServiceImpl implements GatewayRateLimitService {

    @Autowired
    private GatewayRateLimitApisMapper gatewayRateLimitApisMapper;

    /**
     * 查询接口流量限制
     *
     * @return
     */
    @Override
    public PageList<GatewayRateLimitApisDto> findRateLimitApiList() {
        List<GatewayRateLimitApisDto> list = gatewayRateLimitApisMapper.selectIpLimitApisDto();
        return new PageList<>(list);
    }

}
