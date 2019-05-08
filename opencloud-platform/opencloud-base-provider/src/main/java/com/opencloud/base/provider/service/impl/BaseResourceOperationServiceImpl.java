package com.opencloud.base.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.constants.ResourceType;
import com.opencloud.base.client.model.entity.BaseResourceOperation;
import com.opencloud.base.client.model.entity.BaseResourceOperationApi;
import com.opencloud.base.provider.mapper.BaseResourceOperationApiMapper;
import com.opencloud.base.provider.mapper.BaseResourceOperationMapper;
import com.opencloud.base.provider.service.BaseAuthorityService;
import com.opencloud.base.provider.service.BaseResourceOperationService;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.model.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class BaseResourceOperationServiceImpl implements BaseResourceOperationService {
    @Autowired
    private BaseResourceOperationMapper baseResourceOperationMapper;
    @Autowired
    private BaseResourceOperationApiMapper baseResourceOperationApiMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<BaseResourceOperation> findListPage(PageParams pageParams) {
        BaseResourceOperation query =  pageParams.mapToObject(BaseResourceOperation.class);
        QueryWrapper<BaseResourceOperation> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getOperationCode()),BaseResourceOperation::getOperationCode, query.getOperationCode())
                .likeRight(ObjectUtils.isNotEmpty(query.getOperationName()),BaseResourceOperation::getOperationName, query.getOperationName());
        return baseResourceOperationMapper.selectPage(new Page(pageParams.getPage(),pageParams.getLimit()),queryWrapper);
    }

    /**
     * 查询菜单下所有操作
     *
     * @param menuId
     * @return
     */
    @Override
    public List<BaseResourceOperation> findListByMenuId(Long menuId) {
        QueryWrapper<BaseResourceOperation> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseResourceOperation::getMenuId, menuId);
        List<BaseResourceOperation> list = baseResourceOperationMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 根据主键获取Operation
     *
     * @param operationId
     * @return
     */
    @Override
    public BaseResourceOperation getOperation(Long operationId) {
        return baseResourceOperationMapper.selectById(operationId);
    }


    /**
     * 检查Operation编码是否存在
     *
     * @param operationCode
     * @return
     */
    @Override
    public Boolean isExist(String operationCode) {
        QueryWrapper<BaseResourceOperation> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseResourceOperation::getOperationCode, operationCode);
        int count = baseResourceOperationMapper.selectCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加Operation操作
     *
     * @param operation
     * @return
     */
    @Override
    public BaseResourceOperation addOperation(BaseResourceOperation operation) {
        if (isExist(operation.getOperationCode())) {
            throw new OpenAlertException(String.format("%s编码已存在!", operation.getOperationCode()));
        }
        if (operation.getMenuId() == null) {
            operation.setMenuId(0L);
        }
        if (operation.getPriority() == null) {
            operation.setPriority(0);
        }
        if (operation.getStatus() == null) {
            operation.setStatus(BaseConstants.ENABLED);
        }
        if (operation.getIsPersist() == null) {
            operation.setIsPersist(BaseConstants.DISABLED);
        }
        operation.setCreateTime(new Date());
        operation.setServiceId(DEFAULT_SERVICE_ID);
        operation.setUpdateTime(operation.getCreateTime());
        baseResourceOperationMapper.insert(operation);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(operation.getOperationId(), ResourceType.operation);
        return operation;
    }

    /**
     * 修改Operation操作
     *
     * @param operation
     * @return
     */
    @Override
    public BaseResourceOperation updateOperation(BaseResourceOperation operation) {
        BaseResourceOperation saved = getOperation(operation.getOperationId());
        if (saved == null) {
            throw new OpenAlertException(String.format("%s信息不存在", operation.getOperationId()));
        }
        if (!saved.getOperationCode().equals(operation.getOperationCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(operation.getOperationCode())) {
                throw new OpenAlertException(String.format("%s编码已存在!", operation.getOperationCode()));
            }
        }
        if (operation.getMenuId() == null) {
            operation.setMenuId(0L);
        }
        if (operation.getPriority() == null) {
            operation.setPriority(0);
        }
        operation.setUpdateTime(new Date());
        baseResourceOperationMapper.updateById(operation);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(operation.getOperationId(), ResourceType.operation);
        return operation;
    }

    /**
     * 移除Operation
     *
     * @param operationId
     * @return
     */
    @Override
    public void removeOperation(Long operationId) {
        BaseResourceOperation operation = getOperation(operationId);
        if (operation != null && operation.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        removeOperationApi(operationId);
        baseAuthorityService.removeAuthority(operationId, ResourceType.operation);
        baseResourceOperationMapper.deleteById(operationId);
    }

    /**
     * 操作绑定接口资源
     *
     * @param operationId
     * @param apiIds
     * @return
     */
    @Override
    public void addOperationApi(Long operationId, String... apiIds) {
        if (operationId == null) {
            return;
        }
        // 移除操作已绑定接口
        removeOperationApi(operationId);
        if (apiIds != null && apiIds.length > 0) {
            for (String api : apiIds) {
                Long apiId = Long.parseLong(api);
                BaseResourceOperationApi item = new BaseResourceOperationApi();
                item.setOperationId(operationId);
                item.setApiId(apiId);
                baseResourceOperationApiMapper.insert(item);
            }
        }
    }

    /**
     * 查询操作已绑定接口
     *
     * @param operationId
     * @return
     */
    @Override
    public List<BaseResourceOperationApi> findOperationApi(Long operationId) {
        QueryWrapper<BaseResourceOperationApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseResourceOperationApi::getOperationId, operationId);
       return baseResourceOperationApiMapper.selectList(queryWrapper);
    }

    /**
     * 移除操作已绑定接口
     * @param operationId
     */
    @Override
    public void removeOperationApi(Long operationId) {
        QueryWrapper<BaseResourceOperationApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseResourceOperationApi::getOperationId, operationId);
        baseResourceOperationApiMapper.delete(queryWrapper);
    }
}
