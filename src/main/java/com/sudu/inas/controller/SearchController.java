package com.sudu.inas.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SearchController {

    @RequestMapping(value = "/searchbyprefix",method = RequestMethod.GET)
    public String searchByPrefix(){
        return "searchbyprefix";

    }

}
