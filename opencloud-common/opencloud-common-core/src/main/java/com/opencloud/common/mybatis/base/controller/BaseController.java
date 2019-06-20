package com.opencloud.common.mybatis.base.controller;


import cn.hutool.core.util.ObjectUtil;

import com.opencloud.common.mybatis.base.service.IBaseService;
import com.opencloud.common.mybatis.binder.CustomTimestampEditor;
import com.opencloud.common.mybatis.query.CriteriaQuery;
import com.sun.beans.editors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2019/1/4 22:02
 * @desc: 类描述：基础控制器
 */
public class BaseController<Biz extends IBaseService<T>, T> {

    @Autowired
    protected Biz bizService;


    public CriteriaQuery<T> q() {
        CriteriaQuery q = new CriteriaQuery();
        q.select("*");
        return q;
    }

    /**
     * 构建Cascader数据
     */
    protected List<Map<String, Object>> toCascader(List<Map<String, Object>> maps) {
        maps.forEach(map -> {
            Object obj = map.get("isParent");
            Object label = map.get("label");
            Object id = map.get("value");
            map.put("title", label);
            map.put("id", id);
            Integer isParent = (ObjectUtil.isNotNull(obj) && obj.equals(1)) ? 1 : 0;
            if (isParent.equals(1)) {
                map.put("children", new ArrayList<>());
                map.put("loading", false);
            }
        });
        return maps;
    }


    /**
     * 后台接收Date转换
     */
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        datetimeFormat.setLenient(true);

        binder.registerCustomEditor(java.sql.Timestamp.class, new CustomTimestampEditor(datetimeFormat, true));
        binder.registerCustomEditor(Integer.class, new IntegerEditor());
        binder.registerCustomEditor(String.class, new StringEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
        binder.registerCustomEditor(float.class, new FloatEditor());
    }

}