package com.opencloud.base.client.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.common.annotation.TableAlias;
import com.opencloud.common.mybatis.base.entity.AbstractEntity;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统应用-基础信息
 *
 * @author liuyadu
 */
@TableName("base_app")
@TableAlias("app")
public class BaseApp extends AbstractEntity {
    private static final long serialVersionUID = -4606067795040222681L;

    @TableId(type = IdType.ID_WORKER)
    private String appId;

    /**
     * 应用秘钥
     */
    private String appSecret;

    /**
     * app类型：server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用
     */
    private String appType;

    /**
     * 应用图标
     */
    private String appIcon;

    /**
     * app名称
     */
    private String appName;

    /**
     * app英文名称
     */
    private String appNameEn;
    /**
     * 移动应用操作系统：ios-苹果 android-安卓
     */
    private String appOs;


    /**
     * 用户ID:默认为0
     */
    private Long userId;

    /**
     * 用户类型:platform-平台 isp-服务提供商 dev-自研开发者
     */
    private String userType;

    /**
     * app描述
     */
    private String appDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 官方网址
     */
    private String website;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    private Integer isPersist;

    /**
     * @return app_id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取app名称
     *
     * @return app_name - app名称
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置app名称
     *
     * @param appName app名称
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取app英文名称
     *
     * @return app_name_en - app英文名称
     */
    public String getAppNameEn() {
        return appNameEn;
    }

    /**
     * 设置app英文名称
     *
     * @param appNameEn app英文名称
     */
    public void setAppNameEn(String appNameEn) {
        this.appNameEn = appNameEn;
    }

    /**
     * 获取客户端秘钥
     *
     * @return app_secret - 客户端秘钥
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * 设置客户端秘钥
     *
     * @param appSecret 客户端秘钥
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     * @return app_type
     */
    public String getAppType() {
        return appType;
    }

    /**
     * @param appType
     */
    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppOs() {
        return appOs;
    }

    public void setAppOs(String appOs) {
        this.appOs = appOs;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }


    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsPersist() {
        return isPersist;
    }

    public void setIsPersist(Integer isPersist) {
        this.isPersist = isPersist;
    }


}
