package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.GatewayRateLimitApisDto;
import com.github.lyd.base.client.model.entity.GatewayRateLimitApi;
import com.github.lyd.common.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatewayRateLimitApisMapper extends CrudMapper<GatewayRateLimitApi> {

    List<GatewayRateLimitApisDto> selectIpLimitApisDto();

}
