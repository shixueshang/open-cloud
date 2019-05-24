package com.opencloud.base.client.model;

import com.opencloud.base.client.model.entity.BaseUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zyf
 * @date: 2018/11/12 11:35
 * @description:
 */
@Data
public class AppUser extends BaseUser implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;
    private Long accountId;

    private String account;

}
