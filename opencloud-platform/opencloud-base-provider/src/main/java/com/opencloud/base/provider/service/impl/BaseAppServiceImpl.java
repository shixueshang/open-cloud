package com.opencloud.base.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.auth.client.constants.AuthConstants;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.entity.BaseApp;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.provider.mapper.BaseAppMapper;
import com.opencloud.base.provider.service.BaseAppService;
import com.opencloud.base.provider.service.BaseAuthorityService;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.mybatis.query.CriteriaQuery;
import com.opencloud.common.utils.BeanConvertUtils;
import com.opencloud.common.utils.RandomValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/12 16:26
 * @description:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAppServiceImpl extends BaseServiceImpl<BaseAppMapper, BaseApp> implements BaseAppService {

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
    public IPage<BaseApp> findListPage(PageParams pageParams) {
        BaseApp query = pageParams.mapToObject(BaseApp.class);
        CriteriaQuery<BaseApp> cq = new CriteriaQuery(pageParams);
        cq.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getUserId()), BaseApp::getUserId, query.getUserId())
                .eq(ObjectUtils.isNotEmpty(query.getAppType()), BaseApp::getAppType, query.getAppType())
                .eq(ObjectUtils.isNotEmpty(query.getAppId()), BaseApp::getAppId, query.getAppId())
                .likeRight(ObjectUtils.isNotEmpty(query.getAppName()), BaseApp::getAppName, query.getAppName())
                .likeRight(ObjectUtils.isNotEmpty(query.getAppNameEn()), BaseApp::getAppNameEn, query.getAppNameEn());
        cq.select("app.*,user.user_name");
        //关联User表
        cq.createAlias(BaseUser.class);
        cq.orderByDesc("create_time");
        return pageList(cq);
    }

    /**
     * 获取app详情
     *
     * @param appId
     * @return
     */
    @Cacheable(value = "apps", key = "#appId")
    @Override
    public BaseApp getAppInfo(String appId) {
        return baseAppMapper.selectById(appId);
    }

    /**
     * 获取app和应用信息
     *
     * @param appId
     * @return
     */
    @Override
    @Cacheable(value = "apps", key = "'client:'+#appId")
    public BaseClientDetails getAppClientInfo(String appId) {
        BaseClientDetails baseClientDetails = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(appId);
        baseClientDetails.setAuthorities(baseAuthorityService.findAuthorityByApp(appId));
        return baseClientDetails;
    }

    /**
     * 更新应用开发新型
     *
     * @param baseClientDetails
     */
    @CacheEvict(value = {"apps"}, key = "'client:'+#baseClientDetails.clientId")
    @Override
    public BaseClientDetails updateAppClientInfo(BaseClientDetails baseClientDetails) {
        BaseApp app = getAppInfo(baseClientDetails.getClientId());
        Map info = BeanConvertUtils.objectToMap(app);
        baseClientDetails.setAdditionalInformation(info);
        jdbcClientDetailsService.updateClientDetails(baseClientDetails);
        return baseClientDetails;
    }


    /**
     * 添加应用
     *
     * @param app
     * @return 应用信息
     */
    @CachePut(value = "apps", key = "#app.appId")
    @Override
    public BaseApp addAppInfo(BaseApp app) {
        String clientId = String.valueOf(System.currentTimeMillis());
        String clientSecret = RandomValueUtils.uuid();
        app.setAppId(clientId);
        app.setAppSecret(clientSecret);
        app.setCreateTime(new Date());
        app.setUpdateTime(app.getCreateTime());
        if (app.getIsPersist() == null) {
            app.setIsPersist(0);
        }
        baseAppMapper.insert(app);
        Map info = BeanConvertUtils.objectToMap(app);
        // 功能授权
        BaseClientDetails client = new BaseClientDetails();
        client.setClientId(app.getAppId());
        client.setClientSecret(app.getAppSecret());
        client.setAdditionalInformation(info);
        client.setAccessTokenValiditySeconds(AuthConstants.ACCESS_TOKEN_VALIDITY_SECONDS);
        client.setRefreshTokenValiditySeconds(AuthConstants.REFRESH_TOKEN_VALIDITY_SECONDS);
        jdbcClientDetailsService.addClientDetails(client);
        return app;
    }

    /**
     * 修改应用
     *
     * @param app 应用
     * @return 应用信息
     */
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#app.appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#app.appId")
    })
    @Override
    public BaseApp updateInfo(BaseApp app) {
        BaseApp appInfo = getAppInfo(app.getAppId());
        if (appInfo == null) {
            throw new OpenAlertException(app.getAppId() + "应用不存在!");
        }
        app.setUpdateTime(new Date());
        baseAppMapper.updateById(app);
        Map info = BeanConvertUtils.objectToMap(app);
        // 修改客户端信息
        BaseClientDetails client = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(app.getAppId());
        client.setAdditionalInformation(info);
        jdbcClientDetailsService.updateClientDetails(client);
        return app;
    }

    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    })
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
        baseAppMapper.updateById(appInfo);
        jdbcClientDetailsService.updateClientSecret(appInfo.getAppId(), clientSecret);
        return clientSecret;
    }

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    })
    @Override
    public void removeApp(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new OpenAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        // 移除应用权限
        baseAuthorityService.removeAuthorityApp(appId);
        baseAppMapper.deleteById(appInfo.getAppId());
        jdbcClientDetailsService.removeClientDetails(appInfo.getAppId());
    }


}
