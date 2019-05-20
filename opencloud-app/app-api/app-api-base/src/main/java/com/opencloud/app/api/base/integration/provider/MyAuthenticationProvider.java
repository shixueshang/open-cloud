package com.opencloud.app.api.base.integration.provider;

import com.opencloud.app.client.integration.authenticator.third.ThirdIntegrationAuthenticator;
import com.opencloud.app.client.service.impl.UserDetailsServiceImpl;
import com.opencloud.common.security.OpenUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 实现自己的AuthenticationProvider类，用来自定义用户校验机制
 *
 * @author zyf
 * @date 2018/9/5
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsServiceImpl customerDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取表单输入中返回的用户名;
        String userName = (String) authentication.getPrincipal();
        // 获取表单中输入的密码；
        String password = (String) authentication.getCredentials();
        // 这里调用我们的自己写的获取用户的方法；
        OpenUser userInfo = (OpenUser) customerDetailService.loadUserByUsername(userName);
        if (userInfo == null) {
            throw new BadCredentialsException("用户名不存在");
        }
        //第三方登录不验证密码
        if (!ThirdIntegrationAuthenticator.SMS_AUTH_TYPE.contains(userInfo.getAccountType())) {
            // 这里我们还要判断密码是否正确，这里我们的密码使用BCryptPasswordEncoder进行加密的
            if (!new BCryptPasswordEncoder().matches(password, userInfo.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
        }
        // 这里还可以加一些其他信息的判断，比如用户账号已停用等判断。

        Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();
        // 构建返回的用户登录成功的token
        return new UsernamePasswordAuthenticationToken(userInfo, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
//      这里直接改成retrun true;表示是支持这个执行
        return true;
    }
}