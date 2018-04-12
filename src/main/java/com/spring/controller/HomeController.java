package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * home Controller
 * Created by chenxizhong on 2018/4/12.
 */
@Controller
public class HomeController {

    @GetMapping("/index")
    public String index(){
       // model.addAttribute("name","devil");
        return "index";
    }
}
