package com.opencloud.base.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.GatewayRateLimitApisDto;
import com.opencloud.base.client.model.entity.GatewayRateLimitApi;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuyadu
 */
@Mapper
public interface GatewayRateLimitApisMapper extends BaseMapper<GatewayRateLimitApi> {

    List<GatewayRateLimitApisDto> selectRateLimitApisDto();

}
