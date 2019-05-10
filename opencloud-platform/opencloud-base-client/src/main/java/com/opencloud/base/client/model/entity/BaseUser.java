package com.opencloud.base.client.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.common.annotation.TableAlias;
import com.opencloud.common.mybatis.base.entity.AbstractEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户-基础信息
 *
 * @author liuyadu
 */
@Data
@TableAlias("user")
@TableName("base_user")
public class BaseUser extends AbstractEntity {
    private static final long serialVersionUID = -735161640894047414L;
    /**
     * 系统用户ID
     */
    @TableId(type = IdType.ID_WORKER)
    private Long userId;

    /**
     * 登陆名
     */
    private String userName;

    /**
     * 用户类型:platform-平台 isp-服务提供商 dev-自研开发者
     */
    private String userType;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 描述
     */
    private String userDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 注册IP
     */
    private String registerIp;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 状态:0-禁用 1-启用 2-锁定
     */
    private Integer status;

}
