package com.opencloud.base.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.BaseMenuAuthority;
import com.opencloud.base.client.model.entity.BaseUserAuthority;
import com.opencloud.common.security.Authority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseUserAuthorityMapper extends BaseMapper<BaseUserAuthority> {

    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @return
     */
    List<Authority> selectUserGrantedAuthority(@Param("userId") Long userId);

    /**
     * 获取用户已授权权限完整信息
     *
     * @param userId
     * @return
     */
    List<BaseMenuAuthority> selectUserMenuAuthority(@Param("userId") Long userId);
}
