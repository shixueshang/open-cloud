package com.opencloud.base.client.model;

import com.opencloud.base.client.model.entity.BaseUserAccount;

import java.io.Serializable;

/**
 * @author: liuyadu
 * @date: 2018/11/12 11:35
 * @description:
 */
public class UserAccount extends BaseUserAccount implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;

    /**
     * 系统用户资料
     */
    private UserInfo userProfile;

    public UserInfo getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserInfo userProfile) {
        this.userProfile = userProfile;
    }
}
