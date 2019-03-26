package com.sudu.inas.controller;


import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.Event;
import com.sudu.inas.beans.RealEntity;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.service.ObjectService;
import com.sudu.inas.service.RawinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on  17-10-27.
 */
@Controller
public class ObjectController {

    @Autowired
    ObjectService objectService;

    @Autowired
    RawinfoService rawinfoService;


    @RequestMapping(value = "/objectlist",method = RequestMethod.POST)
    public String getSimilarKey(String prefix, Model model){
        List<RealEntity> entityList = null;
        try {
            entityList = objectService.findEntitiesByPrefix(prefix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("entities",entityList);
        return "objectlist";
    }

    @RequestMapping("/object/{objectId}")
    public String getDetailedInfo(@PathVariable String objectId, Model model){
        RealEntity objectById = null;
        try {
//            objectById = objectService.findObjectById(objectId);
            objectById = objectService.findEntityByIdFromEs(objectId);
            model.addAttribute("objectId",objectId);
            ArrayList<Event> events = objectById.getEvents();
            for (int i = 0;i<events.size();i++){
                if ("2050-01-01".equals(events.get(i).getTs())){
                    events.remove(i);
                }
            }
            model.addAttribute("events",events);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String rawText = rawinfoService.findRawText(objectId);
        String rawText = objectById.getRawInfo();
        model.addAttribute("rawtext",rawText);
        return "showdetailed";
    }


    @RequestMapping(value = "/objects",method = RequestMethod.POST)
    public String getObjectDes(String prefix, Model model){
        List<RealEntity> entityList = null;
        try {
            entityList = objectService.findEntitiesByPrefix(prefix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("entities",entityList);
        return "objectdeslist";
    }




}
