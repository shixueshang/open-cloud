package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.AuthorityMenu;
import com.opencloud.base.client.model.entity.BaseAuthorityRole;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import com.opencloud.common.security.Authority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface BaseAuthorityRoleMapper extends SuperMapper<BaseAuthorityRole> {

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<Authority> selectAuthorityByRole(@Param("roleId") Long roleId);

    /**
     * 获取角色菜单权限
     *
     * @param roleId
     * @return
     */
    List<AuthorityMenu> selectAuthorityMenuByRole(@Param("roleId") Long roleId);
}
