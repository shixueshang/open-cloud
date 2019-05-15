package com.opencloud.base.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.constants.ResourceType;
import com.opencloud.base.client.model.entity.BaseResourceApi;
import com.opencloud.base.provider.mapper.BaseResourceApiMapper;
import com.opencloud.base.provider.service.BaseAuthorityService;
import com.opencloud.base.provider.service.BaseResourceApiService;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseResourceApiServiceImpl extends BaseServiceImpl<BaseResourceApiMapper, BaseResourceApi> implements BaseResourceApiService {
    @Autowired
    private BaseResourceApiMapper baseResourceApiMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<BaseResourceApi> findListPage(PageParams pageParams) {
        BaseResourceApi query = pageParams.mapToObject(BaseResourceApi.class);
        QueryWrapper<BaseResourceApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getPath()), BaseResourceApi::getPath, query.getPath())
                .likeRight(ObjectUtils.isNotEmpty(query.getApiName()), BaseResourceApi::getApiName, query.getApiName())
                .likeRight(ObjectUtils.isNotEmpty(query.getApiCode()), BaseResourceApi::getApiCode, query.getApiCode())
                .eq(ObjectUtils.isNotEmpty(query.getServiceId()), BaseResourceApi::getServiceId, query.getServiceId())
                .eq(ObjectUtils.isNotEmpty(query.getStatus()), BaseResourceApi::getStatus, query.getStatus())
                .eq(ObjectUtils.isNotEmpty(query.getIsAuth()), BaseResourceApi::getIsAuth, query.getIsAuth())
                .eq(ObjectUtils.isNotEmpty(query.getIsOpen()), BaseResourceApi::getIsOpen, query.getIsOpen());
        queryWrapper.orderByDesc("create_time");
        return baseResourceApiMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseResourceApi> findAllList(String serviceId) {
        QueryWrapper<BaseResourceApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(serviceId),BaseResourceApi::getServiceId, serviceId);
        List<BaseResourceApi> list = baseResourceApiMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 根据主键获取接口
     *
     * @param apiId
     * @return
     */
    @Override
    public BaseResourceApi getApi(Long apiId) {
        return baseResourceApiMapper.selectById(apiId);
    }


    @Override
    public Boolean isExist(String apiCode) {
        QueryWrapper<BaseResourceApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(BaseResourceApi::getApiCode, apiCode);
        int count = getCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加接口
     *
     * @param api
     * @return
     */
    @Override
    public BaseResourceApi addApi(BaseResourceApi api) {
        if (isExist(api.getApiCode())) {
            throw new OpenAlertException(String.format("%s编码已存在!", api.getApiCode()));
        }
        if (api.getPriority() == null) {
            api.setPriority(0);
        }
        if (api.getStatus() == null) {
            api.setStatus(BaseConstants.ENABLED);
        }
        if (api.getApiCategory() == null) {
            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
        }
        if (api.getIsPersist() == null) {
            api.setIsPersist(0);
        }
        if (api.getIsOpen() == null) {
            api.setIsOpen(0);
        }
        if (api.getIsAuth() == null) {
            api.setIsAuth(0);
        }
        api.setCreateTime(new Date());
        api.setUpdateTime(api.getCreateTime());
        baseResourceApiMapper.insert(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
        return api;
    }

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    @Override
    public BaseResourceApi updateApi(BaseResourceApi api) {
        BaseResourceApi saved = getApi(api.getApiId());
        if (saved == null) {
            throw new OpenAlertException("信息不存在!");
        }
        if (!saved.getApiCode().equals(api.getApiCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(api.getApiCode())) {
                throw new OpenAlertException(String.format("%s编码已存在!", api.getApiCode()));
            }
        }
        if (api.getPriority() == null) {
            api.setPriority(0);
        }
        if (api.getApiCategory() == null) {
            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
        }
        api.setUpdateTime(new Date());
        baseResourceApiMapper.updateById(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
        return api;
    }

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    @Override
    public BaseResourceApi getApi(String apiCode) {
        QueryWrapper<BaseResourceApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(BaseResourceApi::getApiCode, apiCode);
        return baseResourceApiMapper.selectOne(queryWrapper);
    }


    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    @Override
    public void removeApi(Long apiId) {
        BaseResourceApi api = getApi(apiId);
        if (api != null && api.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        baseAuthorityService.removeAuthority(apiId, ResourceType.api);
        baseResourceApiMapper.deleteById(apiId);
    }


    /**
     * 获取数量
     *
     * @param queryWrapper
     * @return
     */
    @Override
    public int getCount(QueryWrapper<BaseResourceApi> queryWrapper) {
        return baseResourceApiMapper.selectCount(queryWrapper);
    }


}
