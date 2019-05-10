package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.entity.GatewayRateLimit;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GatewayRateLimitMapper extends SuperMapper<GatewayRateLimit> {
}
