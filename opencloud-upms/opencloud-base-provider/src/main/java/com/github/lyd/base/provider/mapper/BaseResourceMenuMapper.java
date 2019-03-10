package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.BaseResourceMenuDto;
import com.github.lyd.base.client.model.entity.BaseResourceMenu;
import com.github.lyd.common.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface BaseResourceMenuMapper extends CrudMapper<BaseResourceMenu> {
    List<BaseResourceMenuDto> selectWithActionList();
}
