package com.github.lyd.auth.provider.service.impl;

import com.github.lyd.auth.client.model.ClientDetailsDto;
import com.github.lyd.auth.provider.service.feign.BaseAuthorityRestRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: liuyadu
 * @date: 2018/11/12 16:26
 * @description:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    @Autowired
    private BaseAuthorityRestRemoteService baseAuthorityRestRestService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails details = jdbcClientDetailsService.loadClientByClientId(clientId);
        if (details != null) {
            // 获取应用权限
            List<GrantedAuthority> authorities = baseAuthorityRestRestService.getGrantedAppAuthority(clientId).getData();
            ClientDetailsDto clientDetailsDto = new ClientDetailsDto(details, authorities);
            return clientDetailsDto;
        }
        return null;
    }
}
