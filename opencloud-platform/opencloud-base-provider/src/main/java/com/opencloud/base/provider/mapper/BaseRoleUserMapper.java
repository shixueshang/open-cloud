package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.entity.BaseRole;
import com.opencloud.base.client.model.entity.BaseRoleUser;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuyadu
 */
@Mapper
public interface BaseRoleUserMapper extends SuperMapper<BaseRoleUser> {
    /**
     * 查询系统用户角色
     *
     * @param userId
     * @return
     */
    List<BaseRole> selectRoleUserList(@Param("userId") Long userId);

    /**
     * 查询用户角色ID列表
     * @param userId
     * @return
     */
    List<Long> selectRoleUserIdList(@Param("userId") Long userId);
}
