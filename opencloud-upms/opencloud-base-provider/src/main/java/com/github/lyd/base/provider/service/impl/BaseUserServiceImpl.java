package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.model.BaseUserDto;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.base.client.model.entity.BaseUser;
import com.github.lyd.base.provider.mapper.BaseUserMapper;
import com.github.lyd.base.provider.service.BaseRoleService;
import com.github.lyd.base.provider.service.BaseUserService;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:33
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserServiceImpl implements BaseUserService {

    @Autowired
    private BaseUserMapper baseUserMapper;
    @Autowired
    private BaseRoleService baseRoleService;

    /**
     * 更新系统用户
     *
     * @param userProfile
     * @return
     */
    @Override
    public Long addProfile(BaseUserDto userProfile) {
        baseUserMapper.insertSelective(userProfile);
        if (userProfile != null && userProfile.getRoleIds() != null && userProfile.getRoleIds().size() > 0) {
            baseRoleService.saveMemberRoles(userProfile.getUserId(), userProfile.getRoleIds().toArray(new Long[userProfile.getRoleIds().size()]));
        }
        return userProfile.getUserId();
    }

    /**
     * 更新系统用户
     *
     * @param userProfile
     * @return
     */
    @Override
    public Boolean updateProfile(BaseUserDto userProfile) {
        if (userProfile == null || userProfile.getUserId() == null) {
            return false;
        }
        if (userProfile.getUserId() == null) {
            return false;
        }
        baseRoleService.saveMemberRoles(userProfile.getUserId(), userProfile.getRoleIds().toArray(new Long[userProfile.getRoleIds().size()]));
        return baseUserMapper.updateByPrimaryKeySelective(userProfile) > 0;
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @param keyword
     * @return
     */
    @Override
    public PageList<BaseUser> findListPage(PageParams pageParams, String keyword) {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceOperation.class);
        Example example = builder.build();
        List<BaseUser> list = baseUserMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询列表
     *
     * @param keyword
     * @return
     */
    @Override
    public PageList<BaseUser> findList(String keyword) {
        ExampleBuilder builder = new ExampleBuilder(BaseUser.class);
        Example example = builder.criteria()
                .orLike("userName", keyword).end().build();
        example.orderBy("userId").asc();
        List<BaseUser> list = baseUserMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public BaseUser getProfile(String username) {
        ExampleBuilder builder = new ExampleBuilder(BaseUser.class);
        Example example = builder.criteria()
                .andEqualTo("userName", username)
                .end().build();
        BaseUser saved = baseUserMapper.selectOneByExample(example);
        return saved;
    }

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public BaseUser getProfile(Long userId) {
        return baseUserMapper.selectByPrimaryKey(userId);
    }


}
