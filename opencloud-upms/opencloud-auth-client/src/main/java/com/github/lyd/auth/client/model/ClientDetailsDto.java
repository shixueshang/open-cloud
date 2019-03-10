package com.github.lyd.auth.client.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author: liuyadu
 * @date: 2018/11/2 18:02
 * @description:
 */
public class ClientDetailsDto implements ClientDetails {
    private static final long serialVersionUID = 3725084953460581042L;

    /**
     * 自定义应用权限,com.github.lyd.common.security.OpenGrantedAuthority
     *
     */
    private Collection<GrantedAuthority> authorities;

    private ClientDetails clientDetails;

    public ClientDetailsDto() {
        super();
    }


    public ClientDetailsDto(ClientDetails clientDetails, Collection<GrantedAuthority> authorities) {
        this.clientDetails = clientDetails;
        this.authorities = authorities;
    }

    @Override
    public String getClientId() {
        return this.clientDetails.getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        return this.clientDetails.getResourceIds();
    }

    @Override
    public boolean isSecretRequired() {
        return this.clientDetails.isSecretRequired();
    }

    @Override
    public String getClientSecret() {
        return this.clientDetails.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return this.clientDetails.isScoped();
    }

    @Override
    public Set<String> getScope() {
        return this.clientDetails.getScope();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return this.clientDetails.getAuthorizedGrantTypes();
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return this.clientDetails.getRegisteredRedirectUri();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.clientDetails.getAccessTokenValiditySeconds();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.clientDetails.getRefreshTokenValiditySeconds();
    }

    @Override
    public boolean isAutoApprove(String s) {
        return this.clientDetails.isAutoApprove(s);
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return this.clientDetails.getAdditionalInformation();
    }
}
