package com.github.lyd.base.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.model.BaseAppDto;
import com.github.lyd.base.client.model.entity.BaseApp;
import com.github.lyd.base.provider.mapper.BaseAppMapper;
import com.github.lyd.base.provider.service.BaseAppService;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.base.provider.service.BaseResourceApiService;
import com.github.lyd.base.provider.service.feign.ClientDetailsClientRemote;
import com.github.lyd.common.configuration.GatewayProperties;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.gen.SnowflakeIdGenerator;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.utils.RandomValueUtils;
import com.github.lyd.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
    private ClientDetailsClientRemote clientDetailsClient;
    @Autowired
    private SnowflakeIdGenerator idGenerator;
    @Autowired
    private BaseResourceApiService baseResourceApiService;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private GatewayProperties gatewayProperties;

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
            appDto.setClientInfo(clientDetailsClient.getClient(appInfo.getAppId()).getData());
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
        String clientId = String.valueOf(idGenerator.nextId());
        String clientSecret = RandomValueUtils.uuid();
        app.setAppId(clientId);
        app.setAppSecret(clientSecret);
        app.setCreateTime(new Date());
        app.setUpdateTime(app.getCreateTime());
        if (app.getIsPersist() == null) {
            app.setIsPersist(BaseConstants.DISABLED);
        }
        baseAppMapper.insertSelective(app);
        String clientInfoJson = JSONObject.toJSONString(app);
        // 功能授权
        if(StringUtils.isNotBlank(app.getAuthorities())){
            app.setAuthorities(grantAccess(app.getAppId(), app.getAuthorities().split(",")));
        }
        // 保持客户端信息
        ResultBody<Boolean> resp = clientDetailsClient.addClient(clientId, clientSecret, BaseConstants.DEFAULT_OAUTH2_GRANT_TYPES, "", app.getRedirectUrls(), app.getScopes(), app.getResourceIds(), app.getAuthorities(),app.getaccessTokenValidity(),app.getrefreshTokenValidity(), clientInfoJson);
        if (!resp.isOk()) {
            // 回滚事物
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
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
        int result = baseAppMapper.updateByPrimaryKeySelective(appInfo);
        String clientInfoJson = JSONObject.toJSONString(appInfo);
        // 功能授权
        app.setAuthorities(grantAccess(app.getAppId(), app.getAuthorities().split(",")));
        // 修改客户端信息
        ResultBody<Boolean> resp = clientDetailsClient.updateClient(app.getAppId(), app.getGrantTypes(), "", app.getRedirectUrls(), app.getScopes(), app.getResourceIds(), app.getAuthorities(),app.getaccessTokenValidity(),app.getrefreshTokenValidity(), clientInfoJson);
        if (!resp.isOk()) {
            // 手动事物回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    @Override
    public String restSecret(String appId) {
        if (gatewayProperties.getClientId().equals(appId)) {
            throw new OpenAlertException(String.format("保留数据,不允许修改"));
        }
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new OpenAlertException(appId + "应用不存在!");
        }
        // 生成新的密钥
        String clientSecret = RandomValueUtils.uuid();
        appInfo.setAppSecret(clientSecret);
        appInfo.setUpdateTime(new Date());
        int result = baseAppMapper.updateByPrimaryKeySelective(appInfo);
        ResultBody<Boolean> resp = clientDetailsClient.resetSecret(appInfo.getAppId(), clientSecret);
        if (!resp.isOk()) {
            // 手动事物回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return (result > 0 && resp.isOk()) ? clientSecret : null;
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
     /*   // 移除授权
        baseAuthorityService.removeAuthority(appInfo.getAppId(), AuthorityConstants.AUTHORITY_APP, null);*/
        ResultBody<Boolean> resp = clientDetailsClient.removeClinet(appInfo.getAppId());
        if (!resp.isOk()) {
            // 回滚事物
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 授权功能
     *
     * @param appId    应用ID
     * @param apiCodes api编码
     * @return authorities 授权后的权限标识
     */
    @Override
    public String grantAccess(String appId, String... apiCodes) {
      /*  List<String> apiIds = baseResourceApiService.findIdsByCodes(apiCodes);
        return baseAuthorityService.addAuthority(appId, AuthorityConstants.AUTHORITY_APP, BaseConstants.RESOURCE_TYPE_API, apiIds.toArray(new String[apiIds.size()]));*/
      return null;
    }

}
