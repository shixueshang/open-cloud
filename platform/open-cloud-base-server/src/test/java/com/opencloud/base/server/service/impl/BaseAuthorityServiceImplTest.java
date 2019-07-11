package com.opencloud.base.server.service.impl;

import com.opencloud.base.server.service.BaseActionService;
import com.opencloud.base.server.service.BaseAuthorityService;
import com.opencloud.base.client.model.entity.BaseAuthority;
import com.opencloud.base.server.service.BaseMenuService;
import com.opencloud.common.security.http.OpenRestTemplate;
import com.opencloud.common.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseAuthorityServiceImplTest extends BaseTest {

    @Autowired
    private BaseActionService baseResourceOperationService;
    @Autowired
    private BaseMenuService baseResourceMenuService;
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



}
