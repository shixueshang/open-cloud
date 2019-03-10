package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.constants.ResourceType;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.base.provider.mapper.BaseResourceOperationMapper;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.base.provider.service.BaseResourceOperationService;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
    private BaseAuthorityService baseAuthorityService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @param keyword
     * @return
     */
    @Override
    public PageList<BaseResourceOperation> findListPage(PageParams pageParams, String keyword) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        ExampleBuilder builder = new ExampleBuilder(BaseResourceOperation.class);
        Example example = builder.criteria()
                .orLike("operationCode", keyword)
                .orLike("operationName", keyword).end().build();
        List<BaseResourceOperation> list = baseResourceOperationMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询菜单下所有操作
     * @param menuId
     * @return
     */
    @Override
    public PageList<BaseResourceOperation> findListByMenuId(Long menuId) {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceOperation.class);
        Example example = builder
                .criteria()
                .andEqualTo("menuId", menuId).end()
                .build();
        example.orderBy("operationId").asc().orderBy("priority").asc();
        List<BaseResourceOperation> list = baseResourceOperationMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 根据主键获取Operation
     *
     * @param operationId
     * @return
     */
    @Override
    public BaseResourceOperation getOperation(Long operationId) {
        return baseResourceOperationMapper.selectByPrimaryKey(operationId);
    }


    /**
     * 检查Operation编码是否存在
     *
     * @param operationCode
     * @return
     */
    @Override
    public Boolean isExist(String operationCode) {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceOperation.class);
        Example example = builder.criteria()
                .andEqualTo("operationCode", operationCode)
                .end().build();
        int count = baseResourceOperationMapper.selectCountByExample(example);
        return count > 0 ? true : false;
    }

    /**
     * 添加Operation操作
     *
     * @param operation
     * @return
     */
    @Override
    public Long addOperation(BaseResourceOperation operation) {
        if (isExist(operation.getOperationCode())) {
            throw new OpenAlertException(String.format("%s编码已存在,不允许重复添加", operation.getOperationCode()));
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
        operation.setUpdateTime(operation.getCreateTime());
        baseResourceOperationMapper.insertSelective(operation);
        return operation.getOperationId();
    }

    /**
     * 修改Operation操作
     *
     * @param operation
     * @return
     */
    @Override
    public void updateOperation(BaseResourceOperation operation) {
        BaseResourceOperation savedOperation = getOperation(operation.getOperationId());
        if (savedOperation == null) {
            throw new OpenAlertException(String.format("%sOperation不存在", operation.getOperationId()));
        }
        if (!savedOperation.getOperationCode().equals(operation.getOperationCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(operation.getOperationCode())) {
                throw new OpenAlertException(String.format("%sOperation编码已存在,不允许重复添加", operation.getOperationCode()));
            }
        }
        if (operation.getMenuId() == null) {
            operation.setMenuId(0L);
        }
        if (operation.getPriority() == null) {
            operation.setPriority(0);
        }
        operation.setUpdateTime(new Date());
        baseResourceOperationMapper.updateByPrimaryKeySelective(operation);
        // 同步授权表里的信息
        baseAuthorityService.saveOrUpdateAuthority(operation.getOperationId(), ResourceType.operation);
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
        if (baseAuthorityService.isGranted(operationId, ResourceType.operation)) {
            throw new OpenAlertException(String.format("资源已被授权,不允许删除,取消授权后,再次尝试!"));
        }
        baseAuthorityService.removeAuthority(operationId,ResourceType.operation);
        baseResourceOperationMapper.deleteByPrimaryKey(operationId);
    }


}
