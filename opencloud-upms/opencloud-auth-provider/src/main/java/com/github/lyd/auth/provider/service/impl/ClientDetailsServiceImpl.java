package com.github.lyd.auth.provider.service.impl;

import com.github.lyd.auth.client.model.BaseClientDetailsDto;
import com.github.lyd.auth.client.model.ClientDetailsDto;
import com.github.lyd.auth.provider.service.CustomClientDetailsService;
import com.github.lyd.auth.provider.service.feign.BaseAuthorityRestRemoteService;
import com.github.lyd.common.model.PageList;
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
public class ClientDetailsServiceImpl implements CustomClientDetailsService, ClientDetailsService {

    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    @Autowired
    private BaseAuthorityRestRemoteService baseAuthorityRestRestService;

    /**
     * 查询所有客户端
     *
     * @return
     */
    @Override
    public PageList<BaseClientDetailsDto> findAllList() {
        return new PageList(jdbcClientDetailsService.listClientDetails());
    }

    /**
     * 获取客户端信息
     *
     * @param clientId
     * @return
     */
    @Override
    public BaseClientDetailsDto getClinet(String clientId) {
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            return null;
        }
        BaseClientDetailsDto clientInfo = new BaseClientDetailsDto(clientDetails);
        return clientInfo;
    }

    /**
     * 添加客户端
     *
     * @param clientId          应用ID
     * @param clientSecret      应用秘钥
     * @param grantTypes        授权类型
     * @param autoApproveScopes 自动授权
     * @param redirectUrls      授权重定向地址
     * @param scopes            授权范围
     * @param resourceIds       资源服务ID
     * @param authorities       权限
     * @param clientInfo        客户端附加信息,json字符串
     */
    @Override
    public boolean addClient(String clientId, String clientSecret, String grantTypes, String autoApproveScopes, String redirectUrls, String scopes, String resourceIds, String authorities, Integer accessTokenValidity, Integer refreshTokenValidity, String clientInfo) {
        BaseClientDetailsDto client = new BaseClientDetailsDto(clientId, clientSecret, grantTypes, autoApproveScopes, redirectUrls, scopes, resourceIds, authorities, accessTokenValidity, refreshTokenValidity, clientInfo);
        try {
            jdbcClientDetailsService.addClientDetails(client);
        } catch (Exception e) {
            log.error("addClient error:{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 更新客户端
     *
     * @param clientId          应用ID
     * @param grantTypes        授权类型
     * @param autoApproveScopes 自动授权
     * @param redirectUrls      授权重定向地址
     * @param scopes            授权范围
     * @param resourceIds       资源服务ID
     * @param authorities       权限
     * @param clientInfo        客户端附加信息,json字符串
     */
    @Override
    public boolean updateClient(String clientId, String grantTypes, String autoApproveScopes, String redirectUrls, String scopes, String resourceIds, String authorities, Integer accessTokenValidity, Integer refreshTokenValidity, String clientInfo) {
        BaseClientDetailsDto client = new BaseClientDetailsDto(clientId, null, grantTypes, autoApproveScopes, redirectUrls, scopes, resourceIds, authorities, accessTokenValidity, refreshTokenValidity, clientInfo);
        try {
            jdbcClientDetailsService.updateClientDetails(client);
        } catch (Exception e) {
            log.error("updateClient error:{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 重置秘钥
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    @Override
    public boolean restSecret(String clientId, String clientSecret) {
        try {
            jdbcClientDetailsService.updateClientSecret(clientId, clientSecret);
        } catch (Exception e) {
            log.error("restSecret error:{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除应用
     *
     * @param clientId
     * @return
     */
    @Override
    public boolean removeClinet(String clientId) {
        try {
            jdbcClientDetailsService.removeClientDetails(clientId);
        } catch (Exception e) {
            log.error("deleteClinet error:{}", e.getMessage());
            return false;
        }
        return true;
    }


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
