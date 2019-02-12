package com.funny.autocode.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "database";
    }

    @RequestMapping("/index")
    public String indexnew() {
        return "index";
    }
}
