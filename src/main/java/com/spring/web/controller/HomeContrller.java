package com.spring.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenxizhong on 2018/5/3.
 */
@Controller
public class HomeContrller {

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("name","chenxizhong");
        return "index";
    }

}
