package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.BaseAuthorityDto;
import com.github.lyd.base.client.model.entity.BaseRoleAuthority;
import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.common.security.OpenGrantedAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseRoleAuthorityMapper extends CrudMapper<BaseRoleAuthority> {

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<OpenGrantedAuthority> selectRoleGrantedAuthority(@Param("roleId") Long roleId);

    /**
     * 获取角色已授权权限完整信息
     *
     * @param roleId
     * @return
     */
    List<BaseAuthorityDto> selectRoleGrantedAuthorityDto(@Param("roleId") Long roleId);
}
