package com.opencloud.gateway.provider.mapper;

import com.opencloud.common.mapper.CrudMapper;
import com.opencloud.gateway.client.model.GatewayRateLimitApisDto;
import com.opencloud.gateway.client.model.entity.GatewayRateLimitApi;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface GatewayRateLimitApisMapper extends CrudMapper<GatewayRateLimitApi> {

    List<GatewayRateLimitApisDto> selectRateLimitApisDto();

}
