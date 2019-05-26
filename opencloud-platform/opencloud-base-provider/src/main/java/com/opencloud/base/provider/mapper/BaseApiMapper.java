package com.opencloud.base.provider.mapper;

import com.opencloud.base.client.model.entity.BaseApi;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author liuyadu
 */
@Repository
public interface BaseApiMapper extends SuperMapper<BaseApi> {
}
