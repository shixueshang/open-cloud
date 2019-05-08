package com.opencloud.base.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.entity.GatewayRateLimit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GatewayRateLimitMapper extends BaseMapper<GatewayRateLimit> {
}
