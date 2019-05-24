package com.opencloud.base.client.api;

import com.opencloud.base.client.model.AppUser;
import com.opencloud.base.client.model.UserAccount;
import com.opencloud.base.client.model.UserInfo;
import com.opencloud.common.model.ResultBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liuyadu
 */
public interface BaseUserAccountRemoteApi {
    /**
     * 登录
     *
     * @param username 登录名
     * @return
     */
    @PostMapping("/account/localLogin")
    ResultBody<UserAccount> localLogin(@RequestParam(value = "username") String username);

    /**
     * 注册第三方登录账号
     *
     * @param account
     * @param password
     * @param accountType
     * @return
     */
    @PostMapping("/account/register/thirdParty")
    ResultBody registerThirdPartyAccount(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType,
            @RequestParam(value = "nickName") String nickName
    );

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @PostMapping("/account/reset/password")
    ResultBody resetPassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword") String newPassword
    );

    /**
     * 获取用户详细信息
     * @param userId
     * @return
     */
    @PostMapping("/user/info")
    ResultBody<UserInfo> getUserInfo(@RequestParam(value = "userId") Long userId);
    /**
     * APP登录
     *
     * @param username 登录名
     * @return
     */
    @PostMapping("/account/appLogin")
    ResultBody<UserAccount> appLogin(@RequestParam(value = "username") String username);
    /**
     * APP初始化登录
     *
     * @param userId
     * @return
     */
    @PostMapping("/login/init")
    ResultBody<AppUser> loginInit();
    /**
     * 获取APP用户详细信息
     *
     * @param userId
     * @return
     */
    @PostMapping("/user/appInfo")
    ResultBody<AppUser> getAppUserInfo(@RequestParam(value = "userId") Long userId);



}
