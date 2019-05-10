package com.opencloud.base.client.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.common.security.Authority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/12 11:35
 * @description:
 */
@Data
public class BaseUserDto extends BaseUser implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;
    /**
     * 用户角色
     */
    private Collection<Map> roles = Lists.newArrayList();

    /**
     * 用户权限
     */
    private Collection<Authority> authorities = Lists.newArrayList();

    /**
     * 第三方账号
     */
    private String thirdParty;
    /**
     * 密码凭证
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private String password;
}
