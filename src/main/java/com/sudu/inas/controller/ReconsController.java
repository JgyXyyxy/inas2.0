package com.sudu.inas.controller;


import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.service.ReconsService;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by J on  17-10-23.
 */

@Controller
public class ReconsController {

    @Autowired
    ReconsService reconsService;

    @RequestMapping(value = "/recons",method = RequestMethod.POST)
    public @ResponseBody String saveObjectTimenode(String objectId, String time,String location,String description,String result){
        Entity entity = new Entity();
        entity.setObjectId(objectId);
        DetailedInfo detailedInfo = new DetailedInfo(location, description, result);
        Timenode timenode = new Timenode(time, detailedInfo);
        boolean b = reconsService.saveSingleTimenode(entity.getObjectId(), timenode);
        return "ok";
    }

    @RequestMapping(value = "/newtimenode/{objectId}",method = RequestMethod.GET)
    public String newtimenode(@PathVariable String objectId, Model model){
        model.addAttribute("objectId",objectId);
        return "newtimenode";
    }


}
