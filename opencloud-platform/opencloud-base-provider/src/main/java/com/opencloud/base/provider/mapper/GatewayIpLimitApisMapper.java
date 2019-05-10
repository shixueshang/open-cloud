package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.GatewayIpLimitApisDto;
import com.opencloud.base.client.model.entity.GatewayIpLimitApi;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuyadu
 */
@Mapper
public interface GatewayIpLimitApisMapper extends SuperMapper<GatewayIpLimitApi> {

    List<GatewayIpLimitApisDto> selectIpLimitApisDto(@Param("policyType") int policyType);
}
