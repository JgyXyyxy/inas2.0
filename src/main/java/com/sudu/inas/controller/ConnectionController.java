package com.sudu.inas.controller;


import com.sudu.inas.beans.Connection;
import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.service.ConnectionService;
import com.sudu.inas.service.RelevanceService;
import com.sudu.inas.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by J on  17-11-1.
 */

@Controller
public class ConnectionController {

    @Autowired
    ConnectionService connectionService;

    @Autowired
    TimelineService timelineService;

    @Autowired
    RelevanceService relevanceService;

    @RequestMapping(value = "/editconnection/{idPlusQua}",method = RequestMethod.GET)
    public String editConnection(@PathVariable String idPlusQua, Model model){
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String timePoint = strings[1];
        String connNo = strings[2];
        Connection conncetion = null;
        try {
            conncetion = connectionService.findConncetionByConnNo(objectId, timePoint, Integer.parseInt(connNo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("connection",conncetion);
        return "editconnectionform";
    }

    @RequestMapping(value = "/editconnection/{idPlusQua}",method = RequestMethod.POST)
    public String saveEditedConnection(@PathVariable String idPlusQua,Connection connection){
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String timePoint = strings[1];
        String connNo = strings[2];
        connectionService.editConnectionByConnNo(connection,objectId,timePoint,Integer.parseInt(connNo));
        return "redirect:/timenode/"+objectId+"plus"+timePoint;
    }

    @RequestMapping(value = "/newconnection/{idPlusQua}",method = RequestMethod.GET)
    public String toNewConnection(@PathVariable String idPlusQua, Model model){
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String timePoint = strings[1];
        model.addAttribute("objectId",objectId);
        model.addAttribute("timePoint",timePoint);

        return "newconnection";
    }

    @RequestMapping(value = "/timenode/{idPlusQua}",method = RequestMethod.POST)
    public String addNewConnection(@PathVariable String idPlusQua,String connId,String connTime,String influence){
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String timePoint = strings[1];
        connectionService.addConnection(objectId,timePoint,new Connection(connId,connTime,influence));
        try {
            DetailedInfo detailinfoByTimepoint = timelineService.findDetailinfoByTimepoint(connId, connTime);
            if (null == detailinfoByTimepoint){
                timelineService.insetTimenode(connId,new Timenode(connTime,new DetailedInfo("default","default","default")));
            }
            connectionService.addConnection(connId,connTime,new Connection(objectId,timePoint,influence));
        } catch (Exception e) {
            e.printStackTrace();
        }



        return "redirect:/timenode/"+objectId+"plus"+timePoint;
    }

//    @RequestMapping("/deleteconnection/{idPlusQua}")
//    public String deleteConnection(@PathVariable String idPlusQua) throws Exception {
//        String[] strings = idPlusQua.split("plus");
//        String objectId = strings[0];
//        String timePoint = strings[1];
//        String connNo = strings[2];
//        Connection conncetion = connectionService.findConncetionByConnNo(objectId, timePoint, Integer.parseInt(connNo));
//        Connection attached = connectionService.findConnectionByIdAndTime(conncetion.getConnObjectId(), conncetion.getConntimePoint(), objectId, timePoint);
//        connectionService.delConnectionByConnNo(conncetion.getConnObjectId(),conncetion.getConntimePoint(),attached.getConnNo());
//        connectionService.delConnectionByConnNo(objectId,timePoint,Integer.parseInt(connNo));
//
//    }

    @RequestMapping("/deleterele/{id}")
    public String deleteRelevance(@PathVariable String id) throws Exception {
        String[] split = id.split(":");
        String eventId = split[0];
        String rId = split[1];
        relevanceService.delRelevance(rId);
        return "redirect:/timenode/"+eventId;
    }

}
