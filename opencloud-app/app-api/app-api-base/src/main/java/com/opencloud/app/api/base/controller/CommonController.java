package com.opencloud.app.api.base.controller;

import cn.hutool.core.util.RandomUtil;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.utils.DesUtils;
import com.opencloud.common.utils.RedisUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/9 15:43
 * @description: 通用模块接口, 不需要登录鉴权的接口
 */
@Api(tags = "APP接口-通用接口")
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 获取验证码
     *
     * @return
     */
    @PostMapping("/getSmsCode")
    public ResultBody getLoginToken(@RequestParam String username, @RequestParam String time) {
        //解密手机号
        String mobile = DesUtils.getTel(username, time);
        Integer code = RandomUtil.randomInt(100000, 999999);
        //保存验证码
        redisUtils.set(CommonConstants.PRE_SMS + mobile, code, 900);
        //发送验证码
        Map<String, Integer> params = new HashMap<>();
        params.put("code", code);
        return ResultBody.success("发送成功", params);
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.randomInt(100000, 999999));
    }


}
