package com.github.lyd.task.provider.job;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * 微服务远程调用任务
 *
 * @author liuyadu
 */
@Slf4j
public class HttpExecuteJob implements Job {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println(JSONObject.toJSONString(jobExecutionContext.getJobDetail().getJobDataMap()));
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String serviceId = dataMap.getString("serviceId");
        String method = dataMap.getString("method");
        String path = dataMap.getString("path");
        String url = String.format("http://%s%s", serviceId, path);
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);
    }
}
