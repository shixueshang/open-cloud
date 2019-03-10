package com.github.lyd.base.provider.service;

import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;

/**
 * 操作资源管理
 * @author liuyadu
 */
public interface BaseResourceOperationService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @param keyword
     * @return
     */
    PageList<BaseResourceOperation> findListPage(PageParams pageParams, String keyword);

    /**
     * 根据主键获取操作
     *
     * @param actionId
     * @return
     */
    BaseResourceOperation getOperation(Long actionId);

    /**
     * 查询菜单下所有操作
     * @param menuId
     * @return
     */
    PageList<BaseResourceOperation> findListByMenuId(Long menuId);

    /**
     * 检查操作编码是否存在
     *
     * @param actionCode
     * @return
     */
    Boolean isExist(String actionCode);


    /**
     * 添加操作资源
     *
     * @param action
     * @return
     */
    Long addOperation(BaseResourceOperation action);

    /**
     * 修改操作资源
     *
     * @param action
     * @return
     */
    void updateOperation(BaseResourceOperation action);


    /**
     * 移除操作
     *
     * @param actionId
     * @return
     */
    void removeOperation(Long actionId);
}
