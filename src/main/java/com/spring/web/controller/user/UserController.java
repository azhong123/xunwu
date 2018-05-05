package com.spring.web.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 普通用户
 * Created by chenxizhong on 2018/5/5.
 */
@Controller
public class UserController {

    /**
     * 普通用户登录
     * @return
     */
    @GetMapping("/user/login")
    public String loginPage(){
        return "user/login";
    }

    /**
     * 普通用户页面
     * @return
     */
    @GetMapping("/user/center")
    public String centerPage(){
        return "user/center";
    }

}
