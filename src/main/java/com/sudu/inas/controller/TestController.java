package com.sudu.inas.controller;


import com.sudu.inas.beans.*;
import com.sudu.inas.service.ConnectionService;
import com.sudu.inas.service.ObjectService;
import com.sudu.inas.service.RawinfoService;
import com.sudu.inas.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.rmi.MarshalledObject;
import java.util.*;


@Controller
public class TestController {

    @Autowired
    ObjectService objectService;

    @Autowired
    ConnectionService connectionService;

    @Autowired
    RawinfoService rawinfoService;


    @RequestMapping(value = "/graphtest.do",method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ArrayList> getSingLineGraph(String objectId) throws Exception {
        Map<String, ArrayList> map = singLineGraphforId(objectId,0,null);
        return map;
    }

    @RequestMapping(value = "/graphforconn.do",method = RequestMethod.POST)
    public @ResponseBody
    Map<String,ArrayList> getConnLine(String objectId,String selectedId,int num) throws Exception {
        System.out.println(objectId);
        System.out.println(selectedId);
        System.out.println(num);
        Map<String, ArrayList> map = singLineGraphforId(selectedId,num,objectId);
        return map;
    }


    @RequestMapping(value = "/searchtest.do",method = RequestMethod.POST)
    public @ResponseBody
    Map<String,String> getDesLlist(String prefix) throws Exception {
        HashMap<String, String> desList = new HashMap<>();
        List<Entity> objectsByPrefix = objectService.findObjectsByPrefix(prefix);
        for (Entity entity:objectsByPrefix){
            desList.put(entity.getObjectId(),entity.getRealName());
        }
        return desList;
    }
    @RequestMapping(value = "/saveNewEntitytest.do",method = RequestMethod.POST)
    public @ResponseBody
    Map<String,String> saveNewEntity(String name,String description,String prefix) throws Exception {
        String objectId = name + CommonUtil.genRandomNum();
        String realName = name+" "+description;
        rawinfoService.addRealName(realName,objectId);
        HashMap<String, String> desList = new HashMap<>();
        List<Entity> objectsByPrefix = objectService.findObjectsByPrefix(prefix);
        for (Entity entity:objectsByPrefix){
            desList.put(entity.getObjectId(),entity.getRealName());
        }
        return desList;
    }

    public Map<String, ArrayList> singLineGraphforId(String objectId,int num,String connId) throws Exception {
        HashMap<String, ArrayList> map = new HashMap<>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<String> idList = new ArrayList<>();
        int xBegin = 5;
        int yBegin = 5;
        int x = xBegin;
        int y = yBegin + num*5;
        Entity entity = objectService.findObjectById(objectId);
        if (null == entity){
            return null;
        }
        ArrayList<Timenode> timeLine = entity.getTimeLine();
        for (Timenode timenode:timeLine){
            Node node = new Node();
            if (num == 0){
                node.setColor("red");
            }else {
                node.setColor("blue");
            }
            node.setSize(20);
            node.setLabel(timenode.getTimePoint()+timenode.getInfo());
            String id = objectId+timenode.getTimePoint();
            idList.add(id);
            node.setId(objectId+timenode.getTimePoint());
            node.setY(y);
            x = x+10;
            node.setX(x);
            nodes.add(node);
            if (connId!= null){
                List<Connection> conncetionListByTimePoint = connectionService.findConncetionListByTimePoint(objectId, timenode.getTimePoint());
                if (conncetionListByTimePoint!= null){
                    for (Connection connection:conncetionListByTimePoint){
                        if (connection.getConnObjectId().equals(connId)) {
                            Edge edge = new Edge(id, connId + connection.getConntimePoint());
                            edges.add(edge);
                        }

                    }
                }
            }
        }
        if (idList.size()>1){
            Iterator<String> idIter = idList.iterator();
            String exid = idIter.next();
            while (idIter.hasNext()){
                String id = idIter.next();
                Edge edge = new Edge(exid, id);
                edges.add(edge);
                exid = id;
            }
        }

        map.put("nodes", nodes);
        map.put("edges",edges);
        return map;
    }


}
