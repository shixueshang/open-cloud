package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.BaseAuthorityDto;
import com.github.lyd.base.client.model.entity.BaseAuthority;
import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.common.security.OpenGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface BaseAuthorityMapper extends CrudMapper<BaseAuthority> {

    /**
     * 查询权限列表
     *
     * @param map
     * @return
     */
    List<OpenGrantedAuthority> selectAuthority(Map map);

    /**
     * 查询权限列表详情
     *
     * @param map
     * @return
     */
    List<BaseAuthorityDto> selectBaseAuthorityDto(Map map);
}
