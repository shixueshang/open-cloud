package com.opencloud.base.provider.service.impl;

import com.opencloud.base.client.model.entity.BaseAuthority;
import com.opencloud.base.client.model.entity.BaseMenu;
import com.opencloud.base.client.model.entity.BaseAction;
import com.opencloud.base.provider.service.BaseAuthorityService;
import com.opencloud.base.provider.service.BaseMenuService;
import com.opencloud.base.provider.service.BaseActionService;
import com.opencloud.common.security.http.OpenRestTemplate;
import com.opencloud.common.test.BaseTest;
import com.opencloud.common.utils.RandomValueUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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


    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String clientId = String.valueOf(System.currentTimeMillis());
        String clientSecret = RandomValueUtils.uuid();
        System.out.println(clientId);
        System.out.println(clientSecret);
        System.out.println(encoder.encode(clientSecret));
    }
}
