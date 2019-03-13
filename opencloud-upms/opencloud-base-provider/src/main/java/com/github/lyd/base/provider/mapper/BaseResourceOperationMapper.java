package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.BaseResourceOperationDto;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.common.mapper.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface BaseResourceOperationMapper extends CrudMapper<BaseResourceOperation> {

    /**
     * 根据主键查询操作拓展信息
     *
     * @param id
     * @return
     */
    BaseResourceOperationDto selectOperationDtoByPrimaryKey(@Param("id") Long id);

    /**
     * 条件查询操作拓展信息
     *
     * @param map
     * @return
     */
    List<BaseResourceOperationDto> selectOperationDtoByCondition(Map map);
}
