package com.github.lyd.task.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuyadu
 * @date: 2019/3/29 14:12
 * @description:
 */
@RestController
public class SchedulerController {
    @Autowired
    NacosDiscoveryProperties nacosDiscoveryProperties;

   // nacosDiscoveryProperties.namingServiceInstance();
}
