package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.entity.BaseRole;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Mapper
public interface BaseRoleMapper extends SuperMapper<BaseRole> {

    List<BaseRole> selectRoleList(Map params);
}
