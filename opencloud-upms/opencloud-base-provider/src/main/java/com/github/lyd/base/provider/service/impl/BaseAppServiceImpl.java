package com.github.lyd.base.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.lyd.auth.client.model.BaseClientDetailsDto;
import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.model.BaseAppDto;
import com.github.lyd.base.client.model.entity.BaseApp;
import com.github.lyd.base.provider.mapper.BaseAppMapper;
import com.github.lyd.base.provider.service.BaseAppService;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.security.OpenGrantedAuthority;
import com.github.lyd.common.utils.RandomValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author: liuyadu
 * @date: 2018/11/12 16:26
 * @description:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAppServiceImpl implements BaseAppService {

    @Autowired
    private BaseAppMapper baseAppMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    /**
     * 查询应用列表
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<BaseApp> findListPage(PageParams pageParams, String keyword) {
        ExampleBuilder builder = new ExampleBuilder(BaseApp.class);
        Example example = builder.criteria()
                .orLike("appName", keyword)
                .orLike("appNameEn", keyword).end().build();
        List<BaseApp> list = baseAppMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 获取app详情
     *
     * @param appId
     * @return
     */
    @Override
    public BaseApp getAppInfo(String appId) {
        return baseAppMapper.selectByPrimaryKey(appId);
    }

    /**
     * 获取app和应用信息
     *
     * @param appId
     * @return
     */
    @Override
    public BaseAppDto getAppWithClientInfo(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            return null;
        }
        BaseAppDto appDto = new BaseAppDto();
        BeanUtils.copyProperties(appInfo, appDto);
        try {
            ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(appId);
            if (clientDetails == null) {
                return null;
            }
            BaseClientDetailsDto clientInfo = new BaseClientDetailsDto(clientDetails);
            appDto.setClientInfo(clientInfo);
        } catch (Exception e) {
            log.error("clientDetailsClient.getClient error:{}", e.getMessage());
        }
        return appDto;
    }

    /**
     * 添加应用
     *
     * @param app
     * @return 应用信息
     */
    @Override
    public String addAppInfo(BaseAppDto app) {
        String clientId = String.valueOf(System.currentTimeMillis());
        String clientSecret = RandomValueUtils.uuid();
        app.setAppId(clientId);
        app.setAppSecret(clientSecret);
        app.setCreateTime(new Date());
        app.setUpdateTime(app.getCreateTime());
        if (app.getIsPersist() == null) {
            app.setIsPersist(0);
        }
        baseAppMapper.insertSelective(app);
        String clientInfo = JSONObject.toJSONString(app);
        // 功能授权
        app.setAuthorities(getAuthorities(app.getAppId()));
        BaseClientDetailsDto client = new BaseClientDetailsDto(app.getAppId(), app.getAppSecret(), app.getGrantTypes(), "", app.getRedirectUrls(), app.getScopes(), app.getResourceIds(), app.getAuthorities(), app.getaccessTokenValidity(), app.getaccessTokenValidity(), clientInfo);
        jdbcClientDetailsService.addClientDetails(client);
        return app.getAppId();
    }

    /**
     * 修改应用
     *
     * @param app 应用
     * @return 应用信息
     */
    @Override
    public void updateInfo(BaseAppDto app) {
        BaseApp appInfo = getAppInfo(app.getAppId());
        if (appInfo == null) {
            throw new OpenAlertException(app.getAppId() + "应用不存在!");
        }
        BeanUtils.copyProperties(app, appInfo);
        appInfo.setUpdateTime(new Date());
        baseAppMapper.updateByPrimaryKeySelective(appInfo);
        String clientInfo = JSONObject.toJSONString(appInfo);
        // 更新应用权限
        app.setAuthorities(getAuthorities(app.getAppId()));
        // 修改客户端信息
        BaseClientDetailsDto client = new BaseClientDetailsDto(app.getAppId(), app.getAppSecret(), app.getGrantTypes(), "", app.getRedirectUrls(), app.getScopes(), app.getResourceIds(), app.getAuthorities(), app.getaccessTokenValidity(), app.getaccessTokenValidity(), clientInfo);
        jdbcClientDetailsService.updateClientDetails(client);
    }

    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    @Override
    public String restSecret(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new OpenAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许修改"));
        }
        // 生成新的密钥
        String clientSecret = RandomValueUtils.uuid();
        appInfo.setAppSecret(clientSecret);
        appInfo.setUpdateTime(new Date());
        baseAppMapper.updateByPrimaryKeySelective(appInfo);
        jdbcClientDetailsService.updateClientSecret(appInfo.getAppId(), clientSecret);
        return clientSecret;
    }

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    @Override
    public void removeApp(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new OpenAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        baseAppMapper.deleteByPrimaryKey(appInfo.getAppId());
        jdbcClientDetailsService.removeClientDetails(appInfo.getAppId());
    }

    /**
     * 获取权限标识
     *
     * @param appId
     * @return
     */
    private String getAuthorities(String appId) {
        StringBuffer sbf = new StringBuffer("");
        List<OpenGrantedAuthority> authorities = baseAuthorityService.findAppGrantedAuthority(appId);
        if (authorities != null && authorities.size() > 0) {
            for (OpenGrantedAuthority authority : authorities
                    ) {
                sbf.append(authority.getAuthority()).append(",");
            }
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }
}
