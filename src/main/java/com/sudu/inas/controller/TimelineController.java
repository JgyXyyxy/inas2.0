package com.sudu.inas.controller;

import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TimelineController {

    @Autowired
    TimelineService timelineService;

    @RequestMapping("/timenode/{idPlusQua}")
    public String getTimeNode(@PathVariable String idPlusQua, Model model)throws Exception{
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String timePoint = strings[1];
        DetailedInfo detailInfo = timelineService.findDetailinfoByTimepoint(objectId, timePoint);
        model.addAttribute("detail",detailInfo);
        return "showtimenode";
    }

    @RequestMapping(value = "/edittimenode/{idPlusQua}",method = RequestMethod.GET)
    public String editTimeNode(@PathVariable String idPlusQua, Model model){
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String timePoint = strings[1];
        DetailedInfo detailedInfo = timelineService.findDetailinfoByTimepoint(objectId, timePoint);
        model.addAttribute("detail",detailedInfo);
        return "editnodeform";
    }

    @RequestMapping(value = "/edittimenode/{idPlusQua}",method = RequestMethod.POST)
    public String saveEditedNode(@PathVariable String idPlusQua,DetailedInfo detailedInfo){
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String qualifier = strings[1];
        Timenode timenode = new Timenode();
        timenode.setTimePoint(qualifier);
        timenode.setInfo(detailedInfo);
        timelineService.insetTimenode(objectId,timenode );
        return "redirect:/object/"+objectId;
    }

    @RequestMapping("/deletenode/{idPlusQua}")
    public String deleteTimeNode(@PathVariable String idPlusQua){
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String timePoint = strings[1];
        timelineService.delTimenodeByTimepoint(objectId,timePoint);
        return "redirect:/object/"+objectId;
    }


    @RequestMapping(value = "/newtimenode/{objectId}",method = RequestMethod.GET)
    public String newtimenode(@PathVariable String objectId, Model model){
        model.addAttribute("objectId",objectId);
        return "newtimenode";
    }

    @RequestMapping(value = "/recons",method = RequestMethod.POST)
    public  String saveObjectTimenode(String objectId, String time,String location,String description,String result){
        Entity entity = new Entity();
        entity.setObjectId(objectId);
        DetailedInfo detailedInfo = new DetailedInfo(location, description, result);
        Timenode timenode = new Timenode(time, detailedInfo);
        timelineService.insetTimenode(entity.getObjectId(), timenode);
        return "redirect:/object/"+objectId;
    }

}
