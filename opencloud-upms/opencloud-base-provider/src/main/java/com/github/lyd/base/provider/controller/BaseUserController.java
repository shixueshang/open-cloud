package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.model.BaseUserDto;
import com.github.lyd.base.client.model.entity.BaseRole;
import com.github.lyd.base.client.model.entity.BaseUser;
import com.github.lyd.base.provider.service.BaseUserAccountService;
import com.github.lyd.base.provider.service.BaseRoleService;
import com.github.lyd.base.provider.service.BaseUserService;
import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户信息
 *
 * @author liuyadu
 */
@Api(tags = "系统用户管理")
@RestController
public class BaseUserController {
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private BaseUserAccountService baseUserAccountService;
    @Autowired
    private BaseRoleService baseRoleService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 系统分页用户列表
     *
     * @return
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "显示条数:最大999", paramType = "form"),
            @ApiImplicitParam(name = "keyword", value = "查询字段", paramType = "form"),
    })
    @PostMapping("/user")
    public ResultBody<PageList<BaseUser>> getUserList(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        return ResultBody.success(baseUserService.findListPage(new PageParams(page, limit), keyword));
    }

    /**
     * 获取所有用户列表
     *
     * @param keyword
     * @return
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询字段", paramType = "form"),
    })
    @PostMapping("/user/all")
    public ResultBody<PageList<BaseRole>> getUserAllList(String keyword) {
        return ResultBody.success(baseUserService.findList(keyword));
    }

    /**
     * 添加系统用户
     *
     * @param userName
     * @param password
     * @param nickName
     * @param status
     * @param userType
     * @param email
     * @param mobile
     * @param userDesc
     * @param avatar
     * @return
     */
    @ApiOperation(value = "添加系统用户", notes = "添加系统用户")
    @PostMapping("/user/add")
    public ResultBody<Long> addUser(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar,
            @RequestParam(value = "roleIds", required = false) String roleIds
    ) {
        BaseUserDto user = new BaseUserDto();
        user.setUserName(userName);
        user.setPassword(password);
        user.setNickName(nickName);
        user.setStatus(status);
        user.setUserType(userType);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setUserDesc(userDesc);
        user.setAvatar(avatar);
        if(StringUtils.isNotBlank(roleIds)){
            List<Long> ids = Arrays.asList(roleIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            user.setRoleIds(ids);
        }
        Long userId = baseUserAccountService.register(user);
        return ResultBody.success(userId);
    }

    /**
     * 更新系统用户
     *
     * @param userId
     * @param nickName
     * @param status
     * @param userType
     * @param email
     * @param mobile
     * @param userDesc
     * @param avatar
     * @return
     */
    @ApiOperation(value = "更新系统用户", notes = "更新系统用户")
    @PostMapping("/user/update")
    public ResultBody updateUser(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar,
            @RequestParam(value = "roleIds", required = false) String roleIds
    ) {
        BaseUserDto user = new BaseUserDto();
        user.setUserId(userId);
        user.setNickName(nickName);
        user.setStatus(status);
        user.setUserType(userType);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setUserDesc(userDesc);
        user.setAvatar(avatar);
        if(StringUtils.isNotBlank(roleIds)){
            List<Long> ids = Arrays.asList(roleIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            user.setRoleIds(ids);
        }
        baseUserService.updateProfile(user);
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }


    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取用户角色", notes = "获取用户角色")
    @PostMapping("/user/roles")
    public ResultBody<PageList<BaseRole>> getUserRoles(
            @RequestParam(value = "userId") Long userId
    ) {
        return ResultBody.success(new PageList<>(baseRoleService.getUserRoles(userId)));
    }
}
