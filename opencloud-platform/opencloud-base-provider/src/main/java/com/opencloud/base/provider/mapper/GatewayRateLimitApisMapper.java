package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.GatewayRateLimitApisDto;
import com.opencloud.base.client.model.entity.GatewayRateLimitApi;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuyadu
 */
@Mapper
public interface GatewayRateLimitApisMapper extends SuperMapper<GatewayRateLimitApi> {

    List<GatewayRateLimitApisDto> selectRateLimitApisDto();

}
