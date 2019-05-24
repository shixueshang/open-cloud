package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.IpLimitApi;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuyadu
 */
@Mapper
public interface GatewayIpLimitApisMapper extends SuperMapper<com.opencloud.base.client.model.entity.GatewayIpLimitApi> {

    List<IpLimitApi> selectIpLimitApi(@Param("policyType") int policyType);
}
