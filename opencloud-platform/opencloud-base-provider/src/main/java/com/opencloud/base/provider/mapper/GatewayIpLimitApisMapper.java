package com.opencloud.base.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.GatewayIpLimitApisDto;
import com.opencloud.base.client.model.entity.GatewayIpLimitApi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuyadu
 */
@Mapper
public interface GatewayIpLimitApisMapper extends BaseMapper<GatewayIpLimitApi> {

    List<GatewayIpLimitApisDto> selectIpLimitApisDto(@Param("policyType") int policyType);
}
