package com.opencloud.base.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.BaseResourceApi;
import com.opencloud.common.model.PageParams;

import java.util.List;

/**
 * 接口资源管理
 * @author liuyadu
 */
public interface BaseResourceApiService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<BaseResourceApi> findListPage(PageParams pageParams);

    /**
     * 查询列表
     * @return
     */
    List<BaseResourceApi> findAllList(String serviceId);

    /**
     * 根据主键获取接口
     *
     * @param apiId
     * @return
     */
    BaseResourceApi getApi(Long apiId);


    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    Boolean isExist(String apiCode);

    /**
     * 添加接口
     *
     * @param api
     * @return
     */
    BaseResourceApi addApi(BaseResourceApi api);

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    BaseResourceApi updateApi(BaseResourceApi api);

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    BaseResourceApi getApi(String apiCode);

    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    void removeApi(Long apiId);


    /**
     * 获取数量
     * @param queryWrapper
     * @return
     */
    int getCount(QueryWrapper<BaseResourceApi> queryWrapper);
}
