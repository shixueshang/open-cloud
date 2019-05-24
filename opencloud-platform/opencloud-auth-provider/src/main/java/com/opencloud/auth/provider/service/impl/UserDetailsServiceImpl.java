package com.opencloud.auth.provider.service.impl;

import com.opencloud.auth.provider.service.feign.BaseUserAccountRemoteService;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.UserAccount;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Security用户信息获取实现类
 *
 * @author liuyadu
 */
@Slf4j
@Service("userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private BaseUserAccountRemoteService baseUserAccountRemoteService;
    /**
     * 认证中心名称
     */
    @Value("${spring.application.name}")
    private String AUTH_SERVICE_ID;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        ResultBody<UserAccount> resp = baseUserAccountRemoteService.localLogin(username);
        UserAccount account = resp.getData();
        if (account == null) {
            throw new UsernameNotFoundException("系统用户 " + username + " 不存在!");
        }
        boolean accountNonLocked = account.getUserProfile().getStatus().intValue() != BaseConstants.USER_STATE_LOCKED;
        boolean credentialsNonExpired = true;
        boolean enable = account.getUserProfile().getStatus().intValue() == BaseConstants.USER_STATE_NORMAL ? true : false;
        boolean accountNonExpired = true;
        return new OpenUser(AUTH_SERVICE_ID, account.getUserId(), account.getAccount(), account.getPassword(), account.getUserProfile().getAuthorities(), accountNonLocked, accountNonExpired, enable, credentialsNonExpired,account.getUserProfile().getNickName(),account.getUserProfile().getAvatar());
    }
}
