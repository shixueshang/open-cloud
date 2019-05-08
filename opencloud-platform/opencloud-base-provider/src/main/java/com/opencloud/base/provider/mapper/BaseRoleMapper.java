package com.opencloud.base.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.entity.BaseRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Mapper
public interface BaseRoleMapper extends BaseMapper<BaseRole> {

    List<BaseRole> selectRoleList(Map params);
}
