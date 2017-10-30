package com.sudu.inas.controller;


import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ObjectController {

    @Autowired
    ObjectService objectService;


    @RequestMapping(value = "/objectlist",method = RequestMethod.POST)
    public String getSimilarKey(String prefix, Model model){
        List<Entity> entityList = objectService.findObjectsByPrefix(prefix);
        model.addAttribute("entities",entityList);
        return "objectlist";
    }

    @RequestMapping("/object/{objectId}")
    public String getDetailedInfo(@PathVariable String objectId, Model model){
        Entity objectById = objectService.findObjectById(objectId);
        model.addAttribute("objectId",objectId);
        ArrayList<Timenode> timeLine = objectById.getTimeLine();
        model.addAttribute("timeline",timeLine);
        return "showdetailed";

    }


}
