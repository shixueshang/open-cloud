package com.github.lyd.app.auth.provider.service.impl;

import com.github.lyd.common.security.OpenAuthUser;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Security用户信息获取实现类
 *
 * @author liuyadu
 */
@Slf4j
@Service("userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 认证中心名称
     */
    @Value("${spring.application.name}")
    private String AUTH_SERVICE_ID;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        //自定义用户存储数据来源，可以是从关系型数据库，非关系性数据库，或者其他地方获取用户数据。

        // 设置 权限,可以是从数据库中查找出来的
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("APP_USER"));
        User user = new User("test", "$2a$10$A7EHximvrsa4ESX1uSlkJupbg2PLO2StzDzy67NX4YV25MxmbGvXu", authorities);

        if (user == null) {
            throw new UsernameNotFoundException("系统用户 " + username + " 不存在!");
        }
        boolean accountNonLocked = user.isAccountNonLocked();
        boolean credentialsNonExpired = true;
        boolean enable = user.isEnabled();
        boolean accountNonExpired = true;
        Map userProfile = Maps.newHashMap();
        userProfile.put("nickName","测试用户昵称");
        userProfile.put("sex","男");
        userProfile.put("age","27");
        userProfile.put("address","北京");
        return new OpenAuthUser(AUTH_SERVICE_ID,11111L, user.getUsername(), user.getPassword(), Collections.emptyList(), accountNonLocked, accountNonExpired, enable, credentialsNonExpired, userProfile);
    }
}
