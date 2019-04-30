package com.opencloud.gateway.provider.mapper;

import com.opencloud.common.mapper.CrudMapper;
import com.opencloud.gateway.client.model.entity.GatewayRateLimit;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayRateLimitMapper extends CrudMapper<GatewayRateLimit> {
}
