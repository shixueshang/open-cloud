package com.opencloud.base.provider.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.BaseResourceMenu;
import com.opencloud.base.client.model.entity.BaseResourceOperation;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * 菜单资源管理
 * @author liuyadu
 */
public interface BaseResourceMenuService extends IBaseService<BaseResourceMenu> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<BaseResourceMenu> findListPage(PageParams pageParams);

    /**
     * 查询列表
     * @return
     */
    List<BaseResourceMenu> findAllList();

    /**
     * 根据主键获取菜单
     *
     * @param menuId
     * @return
     */
    BaseResourceMenu getMenu(Long menuId);

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    Boolean isExist(String menuCode);


    /**
     * 添加菜单资源
     *
     * @param menu
     * @return
     */
    BaseResourceMenu addMenu(BaseResourceMenu menu);

    /**
     * 修改菜单资源
     *
     * @param menu
     * @return
     */
    BaseResourceMenu updateMenu(BaseResourceMenu menu);

    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    void removeMenu(Long menuId);
}
