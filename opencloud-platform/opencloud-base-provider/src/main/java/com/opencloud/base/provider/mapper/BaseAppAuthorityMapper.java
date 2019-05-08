package com.opencloud.base.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opencloud.base.client.model.entity.BaseAppAuthority;
import com.opencloud.common.security.Authority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuyadu
 */
@Mapper
public interface BaseAppAuthorityMapper extends BaseMapper<BaseAppAuthority> {

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    List<Authority> selectAppGrantedAuthority(@Param("appId") String appId);
}
