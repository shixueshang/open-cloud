package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.RateLimitApi;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuyadu
 */
@Mapper
public interface GatewayRateLimitApisMapper extends SuperMapper<com.opencloud.base.client.model.entity.GatewayRateLimitApi> {

    List<RateLimitApi> selectRateLimitApi();

}
