package com.sudu.inas.controller;

import com.sudu.inas.beans.RealEntity;
import com.sudu.inas.beans.Tuple;
import com.sudu.inas.service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ParamController {

    @Autowired
    ParamService paramService;

    @RequestMapping("/param/{objectId}")
    public String getParams(@PathVariable String objectId,String realname,Model model){
        List<Tuple> allParams = paramService.getAllParams(objectId);
        model.addAttribute("objectId",objectId);
        model.addAttribute("realName",realname);
        model.addAttribute("allParams",allParams);
        return "param";
    }
}
