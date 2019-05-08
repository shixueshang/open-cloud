package com.opencloud.base.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.entity.GatewayAccessLogs;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuyadu
 */
@Mapper
public interface GatewayLogsMapper extends BaseMapper<GatewayAccessLogs> {
}
