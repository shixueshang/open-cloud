package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.constants.ResourceType;
import com.github.lyd.base.client.model.entity.BaseResourceMenu;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.base.provider.service.BaseResourceMenuService;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.test.BaseTest;
import com.github.lyd.common.utils.RandomValueUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BaseAuthorityServiceImplTest extends BaseTest {

    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private BaseResourceMenuService baseResourceMenuService;

    @Test
    public void saveOrUpdateAuthority() {
      PageList<BaseResourceMenu> pageList =  baseResourceMenuService.findAllList("");
        for (Object object:pageList.getList()) {
            BaseResourceMenu menu = (BaseResourceMenu)object;
            baseAuthorityService.saveOrUpdateAuthority(menu.getMenuId(), ResourceType.menu);
        }
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
        String clientId = String.valueOf(System.currentTimeMillis());
        String clientSecret = RandomValueUtils.uuid();
        System.out.println(clientId);
        System.out.println(clientSecret);
        System.out.println(encoder.encode(clientSecret));
    }
}
