package com.github.lyd.base.client.model;

import com.github.lyd.auth.client.model.BaseClientDetailsDto;
import com.github.lyd.base.client.model.entity.BaseApp;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author liuyadu
 */
public class BaseAppDto extends BaseApp implements Serializable {
    private static final long serialVersionUID = 7902161098976147412L;
    @Transient
    private BaseClientDetailsDto clientInfo;
    private String grantTypes;
    private boolean autoApprove;
    private String scopes;
    private String resourceIds;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;

    public BaseClientDetailsDto getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(BaseClientDetailsDto clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public boolean isAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public Integer getaccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setaccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getrefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setrefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }
}
