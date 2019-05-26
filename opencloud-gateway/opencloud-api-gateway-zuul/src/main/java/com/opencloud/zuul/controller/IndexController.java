package com.opencloud.zuul.controller;

import com.opencloud.zuul.configuration.ApiProperties;
import com.opencloud.zuul.locator.JdbcRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: liuyadu
 * @date: 2018/11/5 16:33
 * @description:
 */
@Controller
public class IndexController {
    @Autowired
    private ApiProperties apiProperties;



    @Value("${spring.application.name}")
    private String serviceId;

    @GetMapping("/")
    public String index() {
        if (apiProperties.getApiDebug()) {
            return "redirect:doc.html";
        }
        return "index";
    }

}
