package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.AccessAuthority;
import com.opencloud.base.client.model.BaseApiAuthority;
import com.opencloud.base.client.model.BaseMenuAuthority;
import com.opencloud.base.client.model.BaseOperationAuthority;
import com.opencloud.base.client.model.entity.BaseAuthority;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import com.opencloud.common.security.Authority;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Mapper
public interface BaseAuthorityMapper extends SuperMapper<BaseAuthority> {

    /**
     * 查询所有访问权限列表
     */
    List<AccessAuthority> selectAccessAuthority();


    /**
     * 查询已授权权限列表
     *
     * @param map
     * @return
     */
    List<Authority> selectAllGrantedAuthority(Map map);


    /**
     * 获取菜单权限
     *
     * @param map
     * @return
     */
    List<BaseMenuAuthority> selectMenuAuthority(Map map);

    /**
     * 获取操作权限
     *
     * @param map
     * @return
     */
    List<BaseOperationAuthority> selectOperationAuthority(Map map);

    /**
     * 获取API权限
     *
     * @param map
     * @return
     */
    List<BaseApiAuthority> selectApiAuthority(Map map);
}
