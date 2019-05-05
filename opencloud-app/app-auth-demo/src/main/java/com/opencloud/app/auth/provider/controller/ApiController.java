package com.opencloud.app.auth.provider.controller;

import com.opencloud.common.model.ResultBody;
import com.opencloud.autoconfigure.security.OpenUser;
import com.opencloud.autoconfigure.security.OpenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuyadu
 * @date: 2018/11/9 15:43
 * @description:
 */
@Api(tags = "移动应用用户中心")
@RestController
public class ApiController {
    /**
     * 获取用户基础信息
     *
     * @return
     */
    @ApiOperation(value = "获取用户基础信息")
    @GetMapping("/user/me")
    public ResultBody getUserProfile() {
        OpenUser user = OpenHelper.getUser();
        return ResultBody.success(user != null ? user : null);
    }


}
