package com.opencloud.common.mybatis.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.EntityMap;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import com.opencloud.common.mybatis.query.CriteriaQuery;
import com.opencloud.common.security.OpenUser;
import com.opencloud.common.utils.ReflectionUtils;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.context.ContextLoader;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author: zyf
 * @date: 2018/12/24 12:49
 * @desc: 父类service
 */
public abstract class BaseServiceImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> {

    @Autowired
    public ApplicationContext applicationContext;
    @Autowired
    private RedisTokenStore redisTokenStore;
    @Resource
    public SqlSessionTemplate sqlSession;

    public void pushEvent(ApplicationEvent applicationEvent) {
        ContextLoader.getCurrentWebApplicationContext().publishEvent(applicationEvent);
    }

    public IPage pageList(CriteriaQuery<?> wrapper) {
        PageParams page = wrapper.getPagerInfo();
        IPage list = this.baseMapper.pageList(page, wrapper);
        EntityMap.setEnumConvertInterceptor(null);
        return list;
    }

    public EntityMap getEntityMap(CriteriaQuery<?> cq) {
        List<EntityMap> maps = baseMapper.getEntityMap(cq);
        if (ObjectUtils.isEmpty(maps)) {
            //避免空对象输出""
            return new EntityMap();
        }
        return maps.get(0);
    }

    public List<EntityMap> listEntityMaps(CriteriaQuery<?> cq) {
        List<EntityMap> map = baseMapper.getEntityMap(cq);
        return map;
    }

    /**
     * 自定义sql查询List<EntityMap>
     */
    public List<EntityMap> selectListEntityMap(String statement, EntityMap map) {
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }
        return sqlSession.selectList(getMapperName() + statement, map);
    }

    /**
     * 自定义sql查询List<EntityMap>
     */
    public List<EntityMap> selectListEntityMap(EntityMap map) {
        if (ObjectUtils.isEmpty(map)) {
            return null;
        }
        return sqlSession.selectList(getMapperName() + "selectListEntityMapByMap", map);
    }

    /**
     * 自定义sql查询List<EntityMap>
     */
    public List<EntityMap> selectListEntityMap(String statement, @Param("ew") CriteriaQuery<?> cq) {

        return sqlSession.selectList(getMapperName() + statement, cq);
    }

    /**
     * 自定义sql查询List<EntityMap>
     */
    public List<EntityMap> selectListEntityMap(@Param("ew") CriteriaQuery<?> cq) {
        return sqlSession.selectList(getMapperName() + "selectListEntityMapByCq", cq);
    }

    /**
     * 获取mapperName
     */
    public String getMapperName() {
        String mapperName = "";
        Class cl = baseMapper.getClass();
        Class<?> interfaces[] = cl.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            mapperName = anInterface.getName();
        }
        return mapperName + ".";
    }

    public List<T> selectAll() {
        return this.baseMapper.selectList(null);
    }

    /**
     * 更新OpenUser
     *
     * @param openUser
     */
    public void updateOpenUser(OpenUser openUser) {
        // 动态更新客户端生成的token
        Collection<OAuth2AccessToken> accessTokens = redisTokenStore.findTokensByClientIdAndUserName(openUser.getClientId(), openUser.getUsername());
        for (OAuth2AccessToken accessToken : accessTokens) {
            // 由于没有set方法,使用反射机制强制赋值
            OAuth2Authentication oAuth2Authentication = redisTokenStore.readAuthentication(accessToken);
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            ReflectionUtils.setFieldValue(authentication, "principal", openUser);
            // 重新保存
            redisTokenStore.storeAccessToken(accessToken, oAuth2Authentication);
        }
    }
}
