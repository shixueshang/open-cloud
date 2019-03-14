package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.constants.ResourceType;
import com.github.lyd.base.client.model.BaseResourceOperationDto;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.base.provider.mapper.BaseResourceOperationMapper;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.base.provider.service.BaseResourceOperationService;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public PageList<BaseResourceOperationDto> findListPage(PageParams pageParams, String keyword) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        Map params = Maps.newHashMap();
        params.put("operationCode",keyword);
        params.put("operationName",keyword);
        List<BaseResourceOperationDto> list = baseResourceOperationMapper.selectOperationDtoByCondition(params);
        return new PageList(list);
    }

    /**
     * 查询菜单下所有操作
     * @param menuId
     * @return
     */
    @Override
    public List<BaseResourceOperationDto> findListByMenuId(Long menuId) {
        Map params = Maps.newHashMap();
        params.put("menuId",menuId);
        List<BaseResourceOperationDto> list = baseResourceOperationMapper.selectOperationDtoByCondition(params);
        return list;
    }

    /**
     * 根据主键获取Operation
     *
     * @param operationId
     * @return
     */
    @Override
    public BaseResourceOperationDto getOperation(Long operationId) {
        return baseResourceOperationMapper.selectOperationDtoByPrimaryKey(operationId);
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
        operation.setUpdateTime(operation.getCreateTime());
        baseResourceOperationMapper.insertSelective(operation);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(operation.getOperationId(), ResourceType.operation);
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
        baseResourceOperationMapper.updateByPrimaryKeySelective(operation);
        // 同步权限表里的信息
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
        baseAuthorityService.removeAuthority(operationId,ResourceType.operation);
        baseResourceOperationMapper.deleteByPrimaryKey(operationId);
    }


}
