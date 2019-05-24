package com.opencloud.app.api.base.service.impl;


import com.opencloud.app.api.base.integration.authenticator.IntegrationAuthenticator;
import com.opencloud.app.api.base.integration.model.IntegrationAuthenticationContext;
import com.opencloud.app.api.base.integration.model.IntegrationParams;
import com.opencloud.base.client.model.UserAccount;
import com.opencloud.base.client.model.UserInfo;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * APP用户认证中心
 */
@Slf4j
@Service("userDetailService")
@Component
public class UserDetailsServiceImpl implements UserDetailsService {


    private List<IntegrationAuthenticator> authenticators;

    @Autowired(required = false)
    public void setIntegrationAuthenticators(List<IntegrationAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    /**
     * 认证中心名称
     */
    @Value("${spring.application.name}")
    private String AUTH_SERVICE_ID;

    @Override
    public UserDetails loadUserByUsername(String username) {
        IntegrationParams integrationAuthentication = Optional.ofNullable(IntegrationAuthenticationContext.get()).orElse(new IntegrationParams());
        integrationAuthentication.setAccountName(username);
        ResultBody<UserAccount> resp = this.authenticate(integrationAuthentication);
        UserAccount account = Optional.ofNullable(resp).map(u -> resp.getData()).orElseThrow(() -> new UsernameNotFoundException("系统用户 " + username + " 不存在!"));
        Optional<UserInfo> userDto = Optional.ofNullable(account.getUserProfile());
        AtomicReference<String> avatar = new AtomicReference<>("");
        AtomicReference<String> nickName = new AtomicReference<>("");
        userDto.ifPresent(user -> {
            avatar.set(user.getAvatar());
            nickName.set(user.getNickName());
        });
        boolean accountNonLocked = true;
        boolean credentialsNonExpired = true;
        boolean enable = true;
        boolean accountNonExpired = true;
        return new OpenUser(AUTH_SERVICE_ID, account.getAccountId(), account.getUserId(), account.getAccount(), account.getPassword(), accountNonLocked, accountNonExpired, enable, credentialsNonExpired, nickName.get(), avatar.get(), account.getAccountType());
    }

    /**
     * 根据登录类型适配具体执行类
     */
    private ResultBody<UserAccount> authenticate(IntegrationParams integrationAuthentication) {
        if (this.authenticators != null) {
            for (IntegrationAuthenticator authenticator : authenticators) {
                if (authenticator.support(integrationAuthentication)) {
                    return authenticator.authenticate(integrationAuthentication);
                }
            }
        }
        return null;
    }
}
