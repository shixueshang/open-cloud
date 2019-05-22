package com.opencloud.base.provider.service.impl;

import com.opencloud.base.provider.service.BaseUserService;
import com.opencloud.common.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: liuyadu
 * @date: 2019/5/22 16:03
 * @description:
 */
public class BaseUserServiceImplTest extends BaseTest {
    @Autowired
    private BaseUserService baseUserService;
    @Test
    public void loginInit() throws Exception {
        baseUserService.loginInit();
        Thread.sleep(100000000);
    }

}