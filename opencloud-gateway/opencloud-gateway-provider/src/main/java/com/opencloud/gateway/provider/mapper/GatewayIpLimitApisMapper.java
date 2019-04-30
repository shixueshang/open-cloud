package com.opencloud.gateway.provider.mapper;

import com.opencloud.common.mapper.CrudMapper;
import com.opencloud.gateway.client.model.GatewayIpLimitApisDto;
import com.opencloud.gateway.client.model.entity.GatewayIpLimitApi;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface GatewayIpLimitApisMapper extends CrudMapper<GatewayIpLimitApi> {

    List<GatewayIpLimitApisDto> selectIpLimitApisDto(@Param("policyType") int policyType);
}
