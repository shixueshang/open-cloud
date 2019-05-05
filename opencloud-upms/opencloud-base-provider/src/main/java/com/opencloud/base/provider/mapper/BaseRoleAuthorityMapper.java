package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.BaseMenuAuthority;
import com.opencloud.base.client.model.entity.BaseRoleAuthority;
import com.opencloud.common.mapper.CrudMapper;
import com.opencloud.common.model.Authority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface BaseRoleAuthorityMapper extends CrudMapper<BaseRoleAuthority> {

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
