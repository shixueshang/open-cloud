package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.GatewayIpLimitApisDto;
import com.github.lyd.base.client.model.entity.GatewayIpLimitApi;
import com.github.lyd.common.mapper.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatewayIpLimitApisMapper extends CrudMapper<GatewayIpLimitApi> {

    List<GatewayIpLimitApisDto> selectIpLimitApisDto(@Param("policyType") int policyType);
}
