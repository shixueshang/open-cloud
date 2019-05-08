package com.opencloud.base.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.entity.BaseUserAccountLogs;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author liuyadu
 */
@Mapper
public interface BaseUserAccountLogsMapper extends BaseMapper<BaseUserAccountLogs> {
}
