package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.BaseAuthorityDto;
import com.github.lyd.base.client.model.entity.BaseAuthority;
import com.github.lyd.common.mapper.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BaseAuthorityMapper extends CrudMapper<BaseAuthority> {

    List<BaseAuthorityDto> selectBaseAuthorityDto(@Param(value = "params") Map params);
}
