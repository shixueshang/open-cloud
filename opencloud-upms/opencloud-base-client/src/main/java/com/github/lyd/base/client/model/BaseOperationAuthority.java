package com.github.lyd.base.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;

import java.util.Objects;

/**
 * 功能权限
 * @author liuyadu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseOperationAuthority extends BaseResourceOperation {
    private static final long serialVersionUID = -691740581827186502L;

    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;


    /**
     * 是否需要安全认证
     */
    private Boolean isAuth = true;

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(!(obj instanceof BaseOperationAuthority)) {
            return false;
        }
        BaseOperationAuthority a = (BaseOperationAuthority) obj;
        return this.authorityId.equals(a.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }
}
