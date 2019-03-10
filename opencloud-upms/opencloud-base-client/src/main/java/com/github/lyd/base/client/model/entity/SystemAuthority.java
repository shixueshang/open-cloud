package com.github.lyd.base.client.model.entity;

import com.github.lyd.common.gen.SnowflakeId;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 权限控制
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@Table(name = "base_authority")
public class SystemAuthority implements Serializable {
    private static final long serialVersionUID = 3218590056425312760L;
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "authority_id")
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 资源ID
     */
    @Column(name = "resource_id")
    private Long resourceId;

    /**
     * 资源类型:api,menu,action
     */
    @Column(name = "resource_type")
    private String resourceType;

    /**
     * 授权权限所有者ID
     */
    @Column(name = "authority_owner")
    private String authorityOwner;
    /**
     * 权限前缀:用户(USER_) 、角色(ROLE_)、APP(APP_)
     */
    @Column(name = "authority_owner_id")
    private String authorityOwnerId;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getAuthorityOwner() {
        return authorityOwner;
    }

    public void setAuthorityOwner(String authorityOwner) {
        this.authorityOwner = authorityOwner;
    }

    public String getAuthorityOwnerId() {
        return authorityOwnerId;
    }

    public void setAuthorityOwnerId(String authorityOwnerId) {
        this.authorityOwnerId = authorityOwnerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
