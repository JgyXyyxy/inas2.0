package com.sudu.inas.controller;


import com.sudu.inas.beans.Connection;
import com.sudu.inas.service.ConnectionService;
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

    @RequestMapping(value = "/newconnection/{idPlusQua}",method = RequestMethod.POST)
    public String addNewConnection(@PathVariable String idPlusQua,String connId,String connTime,String influence){
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String timePoint = strings[1];
        Connection connection = new Connection();
        connection.setInfluence(influence);
        connection.setConntimePoint(connTime);
        connection.setConnObjectId(connId);
        try {
            List<Connection> connections = connectionService.findConncetionListByTimePoint(objectId, timePoint);
            if (null == connections){
                connections = new ArrayList();
            }
            connections.add(connection);
            connectionService.delConnectionListByTimePoint(objectId,timePoint);
            connectionService.addConnectionListByTimePoint(connections,objectId,timePoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/timenode/"+objectId+"plus"+timePoint;
    }

    @RequestMapping("/deleteconnection/{idPlusQua}")
    public String deleteConnection(@PathVariable String idPlusQua){
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String timePoint = strings[1];
        String connNo = strings[2];
        connectionService.delConnectionByConnNo(objectId,timePoint,Integer.parseInt(connNo));
        return "redirect:/timenode/"+objectId+"plus"+timePoint;
    }


}
