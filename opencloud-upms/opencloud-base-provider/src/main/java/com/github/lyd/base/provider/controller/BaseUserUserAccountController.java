package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.api.BaseUserAccountRemoteApi;
import com.github.lyd.base.client.model.BaseUserAccountDto;
import com.github.lyd.base.provider.service.BaseUserAccountService;
import com.github.lyd.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账号管理
 *
 * @author liuyadu
 */
@Slf4j
@Api(tags = "系统用户账号管理")
@RestController
public class BaseUserUserAccountController implements BaseUserAccountRemoteApi {
    @Autowired
    private BaseUserAccountService baseUserAccountService;

    /**
     * 获取登录账号信息
     *
     * @param username 登录名
     * @return
     */
    @ApiOperation(value = "获取账号登录信息", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path"),
    })
    @GetMapping("/account/localLogin")
    @Override
    public ResultBody<BaseUserAccountDto> localLogin(@RequestParam(value = "username") String username) {
        BaseUserAccountDto account = baseUserAccountService.login(username);
        return ResultBody.success(account);
    }

    /**
     * 注册第三方登录账号
     *
     * @param account
     * @param password
     * @param accountType
     * @return
     */
    @ApiOperation(value = "注册第三方登录账号", notes = "仅限系统内部调用")
    @PostMapping("/account/register/thirdParty")
    @Override
    public ResultBody registerThirdPartyAccount(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType
    ) {
        Long userId = baseUserAccountService.register(account, password, accountType);
        return ResultBody.success(userId);
    }


}
