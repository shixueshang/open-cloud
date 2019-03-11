package com.github.lyd.base.client.api;

import com.github.lyd.base.client.model.BaseAuthorityDto;
import com.github.lyd.common.model.ResultBody;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 权限控制API接口
 *
 * @author liuyadu
 */
public interface BaseAuthorityRemoteApi {
    /**
     * 获取可用权限列表
     *
     * @return
     */
    @GetMapping("/authority/list")
    ResultBody<List<BaseAuthorityDto>> getAuthorityList(
            @RequestParam(value = "type",required = false) Integer type,
            @RequestParam(value = "serviceId",required = false) String serviceId);

    /**
     * 获取应用已分配接口权限
     *
     * @param appId
     * @return
     */
    @PostMapping("/authority/granted/app/api")
    ResultBody<List<GrantedAuthority>> getGrantedAppAuthority(
            @RequestParam(value = "appId") String appId
    );
}
