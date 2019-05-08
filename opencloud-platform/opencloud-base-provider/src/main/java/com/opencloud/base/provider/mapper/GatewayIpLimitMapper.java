package com.opencloud.base.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.entity.GatewayIpLimit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GatewayIpLimitMapper extends BaseMapper<GatewayIpLimit> {
}
