package com.opencloud.auth.provider.controller;

import com.opencloud.auth.provider.service.feign.BaseUserAccountRemoteService;
import com.opencloud.auth.provider.service.impl.GiteeAuthServiceImpl;
import com.opencloud.auth.provider.service.impl.QQAuthServiceImpl;
import com.opencloud.auth.provider.service.impl.WechatAuthServiceImpl;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenUser;
import com.opencloud.common.security.OpenHelper;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/9 15:43
 * @description:
 */
@Api(tags = "认证开放接口")
@RestController
public class ApiController {
    @Autowired
    private QQAuthServiceImpl qqAuthService;
    @Autowired
    private WechatAuthServiceImpl wechatAuthService;
    @Autowired
    private GiteeAuthServiceImpl giteeAuthService;
    @Autowired
    private BaseUserAccountRemoteService baseUserAccountRemoteService;

    /**
     * 获取用户基础信息
     *
     * @return
     */
    @ApiOperation(value = "获取用户基础信息")
    @GetMapping("/current/user")
    public ResultBody getUserProfile() {
        OpenUser user = OpenHelper.getUser();
        return baseUserAccountRemoteService.getUserInfo(user.getUserId());
    }

    /**
     * 获取第三方登录配置
     *
     * @return
     */
    @ApiOperation(value = "获取第三方登录配置", notes = "任何人都可访问")
    @GetMapping("/login/config")
    @ResponseBody
    public ResultBody getLoginOtherConfig() {
        Map<String, String> map = Maps.newHashMap();
        map.put("qq", qqAuthService.getAuthorizationUrl());
        map.put("wechat", wechatAuthService.getAuthorizationUrl());
        map.put("gitee", giteeAuthService.getAuthorizationUrl());
        return ResultBody.success(map);
    }

}
