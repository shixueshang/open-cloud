package com.github.lyd.bpm.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.test.BaseTest;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: liuyadu
 * @date: 2019/4/4 11:05
 * @description:
 */
public class ProcessServiceTest extends BaseTest {
    @Autowired
    private ProcessEngineService processService;

    @Test
    public void findProcessDefinition() throws Exception {
        PageList<ProcessDefinition> result = processService.findProcessDefinition("",1,10);
        System.out.println(JSONObject.toJSONString(result));
    }

}