package com.opencloud.base.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.BaseMenuAuthority;
import com.opencloud.base.client.model.entity.BaseRoleAuthority;
import com.opencloud.common.security.Authority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuyadu
 */
@Mapper
public interface BaseRoleAuthorityMapper extends BaseMapper<BaseRoleAuthority> {

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<Authority> selectRoleGrantedAuthority(@Param("roleId") Long roleId);

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<BaseMenuAuthority> selectRoleMenuAuthority(@Param("roleId") Long roleId);
}
