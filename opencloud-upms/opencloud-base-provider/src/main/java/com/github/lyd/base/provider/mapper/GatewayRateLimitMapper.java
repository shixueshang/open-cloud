package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.entity.GatewayRateLimit;
import com.github.lyd.common.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayRateLimitMapper extends CrudMapper<GatewayRateLimit> {
}
