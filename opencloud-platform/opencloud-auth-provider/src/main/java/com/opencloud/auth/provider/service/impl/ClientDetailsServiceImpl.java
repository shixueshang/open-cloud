package com.opencloud.auth.provider.service.impl;

import com.opencloud.auth.provider.service.feign.BaseAppRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private BaseAppRemoteService baseAppRemoteService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails details = baseAppRemoteService.getAppClientInfo(clientId).getData();
        if(details!=null && !"1".equals(details.getAdditionalInformation().get("status").toString())){
            throw new ClientRegistrationException("客户端已被禁用");
        }
        return details;
    }
}
