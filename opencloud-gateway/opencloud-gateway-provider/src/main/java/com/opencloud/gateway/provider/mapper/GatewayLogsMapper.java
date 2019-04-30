package com.opencloud.gateway.provider.mapper;

import com.opencloud.common.mapper.CrudMapper;
import com.opencloud.gateway.client.model.entity.GatewayAccessLogs;
import org.springframework.stereotype.Repository;

/**
 * @author liuyadu
 */
@Repository
public interface GatewayLogsMapper extends CrudMapper<GatewayAccessLogs> {
}
