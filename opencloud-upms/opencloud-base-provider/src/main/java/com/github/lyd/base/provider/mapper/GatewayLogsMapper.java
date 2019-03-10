package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.entity.GatewayAccessLogs;
import com.github.lyd.common.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * @author liuyadu
 */
@Repository
public interface GatewayLogsMapper extends CrudMapper<GatewayAccessLogs> {
}
