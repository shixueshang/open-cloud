package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.entity.GatewayAccessLogs;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuyadu
 */
@Mapper
public interface GatewayLogsMapper extends SuperMapper<GatewayAccessLogs> {
}
