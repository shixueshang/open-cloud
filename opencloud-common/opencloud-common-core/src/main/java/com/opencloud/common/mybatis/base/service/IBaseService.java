package com.opencloud.common.mybatis.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.opencloud.common.mybatis.EntityMap;
import com.opencloud.common.mybatis.query.CriteriaQuery;

import java.util.List;


public interface IBaseService<T> extends IService<T> {
    /**
     * 自定义分页(xml中配置外键链接查询)
     *
     * @param wrapper
     * @return
     */
    IPage pageList(CriteriaQuery<?> wrapper);

    /**
     * 自定义查询单个实体(返回自定义Map类型,便于枚举转换)
     */
    EntityMap getEntityMap(CriteriaQuery<?> wrapper);

    /**
     * 自定义查询单个实体(返回自定义Map类型,便于枚举转换)
     */
    List<EntityMap> listEntityMaps(CriteriaQuery<?> wrapper);
}
