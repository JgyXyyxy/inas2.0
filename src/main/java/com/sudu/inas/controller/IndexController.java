package com.sudu.inas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by J on  17-10-20.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    String index(){
        return "index";
    }

    @RequestMapping(value = "/recons",method = RequestMethod.GET)
    String recons(){
        return "recons";
    }

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    String search(){
        return "searchbypre";
    }
}