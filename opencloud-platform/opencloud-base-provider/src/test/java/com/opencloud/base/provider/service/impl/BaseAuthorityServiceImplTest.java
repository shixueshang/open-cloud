package com.opencloud.base.provider.service.impl;

import com.opencloud.base.client.model.entity.BaseAuthority;
import com.opencloud.base.client.model.entity.BaseResourceMenu;
import com.opencloud.base.client.model.entity.BaseResourceOperation;
import com.opencloud.base.provider.service.BaseAuthorityService;
import com.opencloud.base.provider.service.BaseResourceMenuService;
import com.opencloud.base.provider.service.BaseResourceOperationService;
import com.opencloud.common.security.http.OpenRestTemplate;
import com.opencloud.common.test.BaseTest;
import com.opencloud.common.utils.RandomValueUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class BaseAuthorityServiceImplTest extends BaseTest {

    @Autowired
    private BaseResourceOperationService baseResourceOperationService;
    @Autowired
    private BaseResourceMenuService baseResourceMenuService;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /***
     * 升级2.0.0权限标识测试类
     */
    @Test
    public void updateAuthority2() {
        List<BaseAuthority> list = baseAuthorityService.list();
        for (BaseAuthority a : list
                ) {
            if (a.getAuthority().startsWith("menu:")) {
                a.setAuthority(a.getAuthority().replace("menu:", "MENU_"));
            }
            if (a.getAuthority().startsWith("operation:")) {
                a.setAuthority(a.getAuthority().replace("operation:", "ACTION_"));
            }
            BaseAuthority authority = new BaseAuthority();
            authority.setAuthorityId(a.getAuthorityId());
            authority.setAuthority(a.getAuthority());
            baseAuthorityService.updateById(authority);
        }
    }

    @Test
    public void refreshGateway() {
        openRestTemplate.refreshGateway();
    }


    @Test
    public void insertAuthority() {
        List<BaseResourceMenu> list = baseResourceMenuService.findAllList();
        for (Object object : list) {
            BaseResourceMenu menu = (BaseResourceMenu) object;
            BaseResourceOperation browse = new BaseResourceOperation();
            browse.setMenuId(menu.getMenuId());
            browse.setOperationCode(menu.getMenuCode() + "Browse");
            browse.setOperationName("浏览");
            browse.setOperationDesc("查看列表");
            browse.setIsPersist(1);
            browse.setStatus(1);
            BaseResourceOperation create = new BaseResourceOperation();
            create.setMenuId(menu.getMenuId());
            create.setOperationCode(menu.getMenuCode() + "Create");
            create.setOperationName("创建");
            create.setOperationDesc("新增数据");
            create.setIsPersist(1);
            create.setStatus(1);
            BaseResourceOperation edit = new BaseResourceOperation();
            edit.setMenuId(menu.getMenuId());
            edit.setOperationCode(menu.getMenuCode() + "Edit");
            edit.setOperationName("编辑");
            edit.setOperationDesc("编辑数据");
            edit.setIsPersist(1);
            edit.setStatus(1);
            BaseResourceOperation remove = new BaseResourceOperation();
            remove.setMenuId(menu.getMenuId());
            remove.setOperationName("删除");
            remove.setOperationDesc("删除数据");
            remove.setOperationCode(menu.getMenuCode() + "Remove");
            remove.setIsPersist(1);
            remove.setStatus(1);
            BaseResourceOperation detail = new BaseResourceOperation();
            detail.setMenuId(menu.getMenuId());
            detail.setOperationCode(menu.getMenuCode() + "Detail");
            detail.setOperationName("详情");
            detail.setOperationDesc("查看详情");
            detail.setIsPersist(1);
            detail.setStatus(1);
            try {
                baseResourceOperationService.addOperation(browse);
                baseResourceOperationService.addOperation(create);
                baseResourceOperationService.addOperation(edit);
                baseResourceOperationService.addOperation(remove);
                baseResourceOperationService.addOperation(detail);
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String clientId = String.valueOf(System.currentTimeMillis());
        String clientSecret = RandomValueUtils.uuid();
        System.out.println(clientId);
        System.out.println(clientSecret);
        System.out.println(encoder.encode(clientSecret));
    }
}
