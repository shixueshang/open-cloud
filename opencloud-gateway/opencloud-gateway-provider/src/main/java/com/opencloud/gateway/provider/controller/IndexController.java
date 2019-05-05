package com.opencloud.gateway.provider.controller;

import com.opencloud.autoconfigure.swagger.OpenSwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OpenSwaggerProperties openSwaggerProperties;

    @GetMapping("/")
    public String index() {
        if (openSwaggerProperties.getEnabled()) {
            return "redirect:swagger-ui.html";
        }
        return "index";
    }

}
