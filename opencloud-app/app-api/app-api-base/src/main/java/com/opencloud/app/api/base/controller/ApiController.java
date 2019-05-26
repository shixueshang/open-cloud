package com.opencloud.app.api.base.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.app.api.base.service.feign.BaseUserAccountRemoteService;
import com.opencloud.base.client.model.AppUser;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.common.security.OpenUser;
import com.opencloud.common.security.http.OpenRestTemplate;
import com.opencloud.common.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/9 15:43
 * @description:
 */
@Api(tags = "移动应用用户中心")
@RestController
public class ApiController {

    @Autowired
    private OpenRestTemplate openRestTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private BaseUserAccountRemoteService baseUserAccountRemoteService;

    /**
     * 获取app用户基础信息
     *
     * @return
     */
    @ApiOperation(value = "获取APP用户基础信息")
    @GetMapping("/user/me")
    public ResultBody<AppUser> getUserProfile() {
        OpenUser user = OpenHelper.getUser();
        return baseUserAccountRemoteService.getAppUserInfo(user.getUserId());
    }

    /**
     * app登录
     */
    @PostMapping("/login/token")
    public Object getLoginToken(@RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "avatar", required = false) String avatar,
                                @RequestParam(value = "nickName", required = false) String nickName,
                                @RequestParam(value = "authType", defaultValue = "password") String authType,
                                @RequestHeader HttpHeaders headers) {
        // 使用oauth2密码模式登录.
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("username", username);
        postParameters.add("password", password);
        postParameters.add("authType", authType);
        postParameters.add("avatar", avatar);
        postParameters.add("nickName", nickName);
        postParameters.add("client_id", "1552274783265");
        postParameters.add("client_secret", "2cde1eaa60fe4af1987f94caa13f29a2");
        postParameters.add("grant_type", "password");
        // 使用客户端的请求头,发起请求
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 强制移除 原来的请求头,防止token失效
        headers.remove(HttpHeaders.AUTHORIZATION);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity(postParameters, headers);
        Map result = openRestTemplate.postForObject("http://localhost:7211/oauth/token", request, Map.class);
        if (ObjectUtils.isNotEmpty(request) && result.containsKey("access_token")) {
            return ResultBody.ok().data(result);
        } else {
            return result;
        }
    }

    /**
     * 登录后初始化数据
     */
    @GetMapping("/login/init")
    public ResultBody<AppUser> loginInit() {
        return baseUserAccountRemoteService.loginInit();
    }

}
