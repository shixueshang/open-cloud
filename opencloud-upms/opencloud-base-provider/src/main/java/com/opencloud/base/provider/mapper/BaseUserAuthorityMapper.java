package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.BaseMenuAuthority;
import com.opencloud.base.client.model.entity.BaseUserAuthority;
import com.opencloud.common.mapper.CrudMapper;
import com.opencloud.common.security.OpenGrantedAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseUserAuthorityMapper extends CrudMapper<BaseUserAuthority> {

    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @return
     */
    List<OpenGrantedAuthority> selectUserGrantedAuthority(@Param("userId") Long userId);

    /**
     * 获取用户已授权权限完整信息
     *
     * @param userId
     * @return
     */
    List<BaseMenuAuthority> selectUserMenuAuthority(@Param("userId") Long userId);
}
