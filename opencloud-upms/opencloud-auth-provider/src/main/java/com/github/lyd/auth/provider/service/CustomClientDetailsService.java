package com.github.lyd.auth.provider.service;

import com.github.lyd.common.model.PageList;
import com.github.lyd.auth.client.model.BaseClientDetailsDto;

/**
 * app信息维护
 *
 * @author: liuyadu
 * @date: 2018/11/12 16:26
 * @description:
 */
public interface CustomClientDetailsService {

    /**
     * 查询所有客户端
     *
     * @return
     */
    PageList<BaseClientDetailsDto> findAllList();

    /**
     * 获取客户端信息
     *
     * @param clientId
     * @return
     */
    BaseClientDetailsDto getClinet(String clientId);

    /**
     * 添加客户端
     *
     * @param clientId          客户端ID
     * @param clientSecret      客户端秘钥
     * @param grantTypes        授权类型
     * @param autoApproveScopes 自动授权
     * @param redirectUrls      授权重定向地址
     * @param scopes            授权范围
     * @param resourceIds       资源服务ID
     * @param authorities       权限
     * @param accessTokenValidity 令牌有效期.秒
     * @param refreshTokenValidity 刷新令牌有效期.秒
     * @param clientInfo        客户端附加信息,json字符串
     */
    boolean addClient(String clientId,
                      String clientSecret,
                      String grantTypes,
                      String autoApproveScopes,
                      String redirectUrls,
                      String scopes,
                      String resourceIds,
                      String authorities,
                      Integer accessTokenValidity,
                      Integer refreshTokenValidity,
                      String clientInfo);

    /**
     * 更新客户端
     *
     * @param clientId          客户端ID
     * @param grantTypes        授权类型
     * @param autoApproveScopes 自动授权
     * @param redirectUrls      授权重定向地址
     * @param scopes            授权范围
     * @param resourceIds       资源服务ID
     * @param authorities       权限
     * @param accessTokenValidity 令牌有效期.秒
     * @param refreshTokenValidity 刷新令牌有效期.秒
     * @param clientInfo        客户端附加信息,json字符串
     */
    boolean updateClient(String clientId,
                         String grantTypes,
                         String autoApproveScopes,
                         String redirectUrls,
                         String scopes,
                         String resourceIds,
                         String authorities,
                         Integer accessTokenValidity,
                         Integer refreshTokenValidity,
                         String clientInfo);

    /**
     * 重置秘钥
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    boolean restSecret(String clientId, String clientSecret);

    /**
     * 删除客户端
     *
     * @param clientId
     * @return
     */
    boolean removeClinet(String clientId);
}
