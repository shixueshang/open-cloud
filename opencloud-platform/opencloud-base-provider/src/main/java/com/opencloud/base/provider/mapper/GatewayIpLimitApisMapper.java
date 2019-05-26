package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.IpLimitApi;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface GatewayIpLimitApisMapper extends SuperMapper<com.opencloud.base.client.model.entity.GatewayIpLimitApi> {

    List<IpLimitApi> selectIpLimitApi(@Param("policyType") int policyType);
}
