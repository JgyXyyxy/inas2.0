package com.sudu.inas.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by J on  17-10-27.
 */

@Controller
public class SearchController {

    @RequestMapping(value = "/searchbyprefix",method = RequestMethod.GET)
    public String searchByPrefix(){
        return "searchbyprefix";

    }

    @RequestMapping(value = "/searchgraph",method = RequestMethod.GET)
    public String searchGraph(){
        return "searchgraph";

    }


    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public String search(){
        return "search";

    }
}
