package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.RateLimitApi;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface GatewayRateLimitApisMapper extends SuperMapper<com.opencloud.base.client.model.entity.GatewayRateLimitApi> {

    List<RateLimitApi> selectRateLimitApi();

}
