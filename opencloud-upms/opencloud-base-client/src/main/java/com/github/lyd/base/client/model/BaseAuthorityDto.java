package com.github.lyd.base.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.constants.ResourceType;
import com.github.lyd.base.client.model.entity.BaseResourceApi;
import com.github.lyd.base.client.model.entity.BaseResourceMenu;

import java.io.Serializable;
import java.util.Date;

/**
 * 授权资源信息
 * @author liuyadu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseAuthorityDto implements Serializable {

    private static final long serialVersionUID = -4808517112300149563L;

    /**
     * 菜单资源
     */
    private BaseResourceMenu menu;

    /**
     * 接口资源
     */
    private BaseResourceApi api;

    /**
     * 操作资源
     */
    private BaseResourceOperationDto operation;

    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 资源类型
     */
    private String resourceType;

    /**
     * 服务名
     */
    private String serviceId;

    /**
     * 是否需要安全认证
     */
    private Boolean isAuth;

    /**
     * 请求路径
     */
    private String path;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof BaseAuthorityDto ? this.authority.equals(((BaseAuthorityDto) obj).authority) : false;
        }
    }

    public String getResourceType() {
        if (menu != null) {
            resourceType = ResourceType.menu.name();
        }
        if (operation != null) {
            resourceType = ResourceType.operation.name();
        }
        if (api != null) {
            resourceType =  ResourceType.api.name();
        }
        return resourceType;
    }

    public String getPath() {
        if (menu != null) {
            path = menu.getPrefix() + menu.getPath();
        }
        if (operation != null) {
            path = operation.getPath();
        }
        if (api != null) {
            path = api.getPath();
        }
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public BaseResourceMenu getMenu() {
        return menu;
    }

    public void setMenu(BaseResourceMenu menu) {
        this.menu = menu;
    }

    public BaseResourceApi getApi() {
        return api;
    }

    public void setApi(BaseResourceApi api) {
        this.api = api;
    }

    public BaseResourceOperationDto getOperation() {
        return operation;
    }

    public void setOperation(BaseResourceOperationDto operation) {
        this.operation = operation;
    }

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

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 是否已过期
     *
     * @return
     */
    public Boolean getExpired() {
        if (expireTime != null && System.currentTimeMillis() < expireTime.getTime()) {
            return true;
        }
        return false;
    }

    public Boolean getAuth() {
        if (api != null) {
            isAuth = api.getIsAuth().equals(BaseConstants.ENABLED);
        } else {
            isAuth = true;
        }
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }
}
