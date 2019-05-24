package com.opencloud.base.client.api;

import com.opencloud.base.client.model.AuthorityAccess;
import com.opencloud.base.client.model.AuthorityMenu;
import com.opencloud.common.model.ResultBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 权限控制API接口
 *
 * @author liuyadu
 */
public interface BaseAuthorityRemoteApi {
    /**
     * 获取所有访问权限列表
     * @return
     */
    @GetMapping("/authority/access")
    ResultBody<List<AuthorityAccess>> findAuthorityAccess();

    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @GetMapping("/authority/menu")
    ResultBody<List<AuthorityMenu>> findAuthorityMenu();
}
