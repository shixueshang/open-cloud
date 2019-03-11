package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.api.BaseAuthorityRemoteApi;
import com.github.lyd.base.client.model.BaseAuthorityDto;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.common.constants.AuthorityConstants;
import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.security.OpenGrantedAuthority;
import com.github.lyd.common.security.OpenHelper;
import com.github.lyd.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author: liuyadu
 * @date: 2018/11/26 18:20
 * @description:
 */
@Api(tags = "系统权限管理")
@RestController
public class BaseAuthorityController implements BaseAuthorityRemoteApi {

    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取权限列表
     *
     * @return 应用信息
     */
    @ApiOperation(value = "获取权限列表", notes = "获取权限列表")
    @GetMapping("/authority/list")
    @Override
    public ResultBody<List<BaseAuthorityDto>> getAuthorityList(
            @RequestParam(value = "type",required = false) Integer type,
            @RequestParam(value = "serviceId",required = false) String serviceId
    ) {
        return ResultBody.success(baseAuthorityService.findAuthorityDto(type, serviceId));
    }


    /**
     * 分配角色权限
     *
     * @param roleId       角色ID
     * @param expireTime   授权过期时间
     * @param authorityIds 权限ID.多个以,隔开
     * @return
     */
    @ApiOperation(value = "分配角色权限", notes = "分配角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/authority/grant/role")
    public ResultBody grantRoleAuthority(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "expireTime", required = false) Date expireTime,
            @RequestParam(value = "authorityIds", required = false) String authorityIds
    ) {
        baseAuthorityService.addRoleAuthority(roleId, expireTime, StringUtils.isNotBlank(authorityIds) ? authorityIds.split(",") : new String[]{});
        // 刷新王国
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }


    /**
     * 分配用户权限
     *
     * @param userId       用户ID
     * @param expireTime   授权过期时间
     * @param authorityIds 权限ID.多个以,隔开
     * @return
     */
    @ApiOperation(value = "分配用户权限", notes = "分配用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/authority/grant/user")
    public ResultBody grantUserAuthority(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "expireTime", required = false) Date expireTime,
            @RequestParam(value = "authorityIds", required = false) String authorityIds
    ) {
        baseAuthorityService.addUserAuthority(userId, expireTime, StringUtils.isNotBlank(authorityIds) ? authorityIds.split(",") : new String[]{});
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }


    /**
     * 分配应用权限
     *
     * @param appId        应用Id
     * @param expireTime   授权过期时间
     * @param authorityIds 权限ID.多个以,隔开
     * @return
     */
    @ApiOperation(value = "分配应用权限", notes = "分配应用权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间.选填", defaultValue = "", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authorityIds", value = "权限ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/authority/grant/app")
    public ResultBody grantAppAuthority(
            @RequestParam(value = "appId") String appId,
            @RequestParam(value = "expireTime", required = false) Date expireTime,
            @RequestParam(value = "authorityIds", required = false) String authorityIds
    ) {
        baseAuthorityService.addAppAuthority(appId, expireTime, StringUtils.isNotBlank(authorityIds) ? authorityIds.split(",") : new String[]{});
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }


    /**
     * 获取角色已分配权限
     *
     * @param roleId 角色ID
     * @return
     */
    @ApiOperation(value = "获取角色已分配权限", notes = "获取角色已分配权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form")
    })
    @PostMapping("/authority/granted/role")
    public ResultBody<List<GrantedAuthority>> getGrantedRoleAuthority(Long roleId) {
        List<OpenGrantedAuthority> result = baseAuthorityService.findRoleGrantedAuthority(roleId);
        return ResultBody.success(result);
    }


    /**
     * 获取用户已分配权限
     *
     * @param userId 用户ID
     * @return
     */
    @ApiOperation(value = "获取用户已分配权限", notes = "获取用户已分配权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", defaultValue = "", required = true, paramType = "form")
    })
    @PostMapping("/authority/granted/user")
    public ResultBody<List<GrantedAuthority>> getGrantedUserAuthority(
            @RequestParam(value = "userId") Long userId
    ) {
        List<OpenGrantedAuthority> result = baseAuthorityService.findUserGrantedAuthority(userId,false);
        return ResultBody.success(result);
    }


    /**
     * 获取应用已分配接口权限
     *
     * @param appId 角色ID
     * @return
     */
    @ApiOperation(value = "获取应用已分配接口权限", notes = "获取应用已分配接口权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", defaultValue = "", required = true, paramType = "form")
    })
    @PostMapping("/authority/granted/app")
    @Override
    public ResultBody<List<GrantedAuthority>> getGrantedAppAuthority(
            @RequestParam(value = "appId") String appId
    ) {
        List<OpenGrantedAuthority> result = baseAuthorityService.findAppGrantedAuthority(appId);
        return ResultBody.success(new PageList(result));
    }

    /**
     * 获取登陆用户已分配权限
     *
     * @return
     */
    @ApiOperation(value = "获取登陆用户已分配权限", notes = "获取登陆用户已分配权限")
    @GetMapping("/authority/granted/me")
    public ResultBody<List<BaseAuthorityDto>> getGrantedMeAuthority() {
        List<BaseAuthorityDto> result = baseAuthorityService.findUserGrantedAuthorityDetail(OpenHelper.getAuthUser().getUserId(), AuthorityConstants.ROOT.equals(OpenHelper.getAuthUser().getUsername()));
        return ResultBody.success(result);
    }


}
