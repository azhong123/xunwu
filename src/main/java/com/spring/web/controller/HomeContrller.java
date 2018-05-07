package com.spring.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 测试
 * Created by chenxizhong on 2018/5/3.
 */
@Controller
public class HomeContrller {

    @GetMapping("/")
    public String index(Model model){
        return "index";
    }

    @GetMapping("/403")
    public String accessError(){
        return "403";
    }

    @GetMapping("/404")
    public String notFoundPage(){
        return "404";
    }

    @GetMapping("/500")
    public String internalError(){
        return "500";
    }

    @GetMapping("/logout/page")
    public String logoutPage(){
        return "logout";
    }




}
