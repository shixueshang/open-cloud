package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.entity.BaseRole;
import com.opencloud.common.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface BaseRoleMapper extends CrudMapper<BaseRole> {

    List<BaseRole> selectRoleList(Map params);
}
