package com.github.lyd.base.provider.service;

import com.github.lyd.base.client.model.BaseResourceOperationDto;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;

import java.util.List;

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
    PageList<BaseResourceOperationDto> findListPage(PageParams pageParams, String keyword);

    /**
     * 根据主键获取操作
     *
     * @param operationId
     * @return
     */
    BaseResourceOperationDto getOperation(Long operationId);

    /**
     * 查询菜单下所有操作
     * @param menuId
     * @return
     */
    List<BaseResourceOperationDto> findListByMenuId(Long menuId);

    /**
     * 检查操作编码是否存在
     *
     * @param operationCode
     * @return
     */
    Boolean isExist(String operationCode);


    /**
     * 添加操作资源
     *
     * @param operation
     * @return
     */
    Long addOperation(BaseResourceOperation operation);

    /**
     * 修改操作资源
     *
     * @param operation
     * @return
     */
    void updateOperation(BaseResourceOperation operation);


    /**
     * 移除操作
     *
     * @param operationId
     * @return
     */
    void removeOperation(Long operationId);
}
