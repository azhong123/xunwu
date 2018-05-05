package com.spring.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 后台
 * Created by chenxizhong on 2018/5/5.
 */
@Controller
public class AdminController {

    /**
     * 后台主页
     * @return
     */
    @GetMapping("/admin/center")
    public String adminCenterPage(){
        return "admin/center";
    }

    /**
     * 后台欢迎页
     * @return
     */
    @GetMapping("/admin/welcome")
    public String welcomePage(){
        return "admin/welcome";
    }

    /**
     * 管理员登陆
     * @return
     */
    @GetMapping("/admin/login")
    public String adminLoginPage(){
        return "admin/login";
    }


}
