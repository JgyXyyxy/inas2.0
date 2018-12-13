package com.sudu.inas.controller;


import com.sudu.inas.beans.*;
import com.sudu.inas.service.*;
import com.sudu.inas.util.CommonUtil;

import org.apache.hadoop.mapreduce.ID;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
public class TestController {

    @Autowired
    ObjectService objectService;

    @Autowired
    ConnectionService connectionService;

    @Autowired
    RawinfoService rawinfoService;

    @Autowired
    TimelineService timelineService;

    @Autowired
    RelevanceService relevanceService;

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
        timelineService.insetTimenode(objectId,new Timenode("0000-00-00",new DetailedInfo("",realName,"")));
        HashMap<String, String> desList = new HashMap<>();
        List<Entity> objectsByPrefix = objectService.findObjectsByPrefix(prefix);
        for (Entity entity:objectsByPrefix){
            desList.put(entity.getObjectId(),entity.getRealName());
        }
        return desList;
    }

    public Map<String, ArrayList> singLineGraphforId(String objectId,int num,String releTag) throws Exception {
        HashMap<String, ArrayList> map = new HashMap<>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<String> idList = new ArrayList<>();
        int xBegin = 5;
        int yBegin = 5;
        int x = xBegin;
        int y = yBegin + num*5;
//        Entity entity = objectService.findObjectById(objectId);
        RealEntity realEntity = objectService.findEntityByIdFromEs(objectId);
        if (null == realEntity){
            return null;
        }
        ArrayList<Event> events = realEntity.getEvents();
        for (Event event:events){
            Node node = new Node();
            if (num == 0){
                node.setColor("red");
            }else {
                node.setColor("blue");
            }
            node.setSize(20);
            node.setLabel(event.toString());
            if (event.getTs().equals("2050-01-01")){
                node.setLabel(realEntity.getRealName());
            }
            node.setSerial(CommonUtil.toDays(event.getTs()));
            idList.add(event.getEventId());
            node.setId(event.getEventId());
            node.setY(y);
            x = x+10;
            node.setX(x);
            nodes.add(node);
//            if (connId!= null){
//                List<Connection> conncetionListByTimePoint = connectionService.findConncetionListByTimePoint(objectId, timenode.getTimePoint());
//                if (conncetionListByTimePoint!= null){
//                    for (Connection connection:conncetionListByTimePoint){
//                        if (connection.getConnObjectId().equals(connId)) {
//                            Edge edge = new Edge(id, connId + connection.getConntimePoint(),connection.getInfluence());
//                            edges.add(edge);
//                        }
//
//                    }
//                }
//            }
        }
        if (idList.size()>1){
            Iterator<String> idIter = idList.iterator();
            String exid = idIter.next();
            while (idIter.hasNext()){
                String id = idIter.next();
                Edge edge = new Edge(exid, id,"");
                edges.add(edge);
                exid = id;
            }
        }

        map.put("nodes", nodes);
        map.put("edges",edges);
        return map;
    }

    @RequestMapping(value = "/commit.do",method = RequestMethod.POST)
    public @ResponseBody String commitEdit(String newNodes,String newEdges,String rawinfo){
        try {
            JSONArray nodeArray = new JSONArray(newNodes);
            for (int i = 0;i<nodeArray.length();i++){
                JSONObject node = (JSONObject) nodeArray.get(i);
                DetailedInfo info = new DetailedInfo(node.getString("location"), node.getString("description"), "");
                Timenode timenode = new Timenode(node.getString("timepoint"), info);
                System.out.print(node.getString("id")+"  ");
                System.out.println(timenode);
            }
            JSONArray edgeArray = new JSONArray(newEdges);
            for (int i = 0;i<edgeArray.length();i++){
                JSONObject edge = (JSONObject) edgeArray.get(i);
                Connection connection = new Connection(edge.getString("targetID"), edge.getString("targetTime"), edge.getString("influence"));
                System.out.print(edge.getString("sourceID")+" ");
                System.out.print(edge.getString("sourceTime")+" ");
                System.out.print(edge.getString("influence")+" ");
                System.out.println(connection);
//                connectionService.addConnection(edge.getString("sourceID"),edge.getString("sourceTime"),connection);
            }

            System.out.println(rawinfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "OK";
    }

    public static void main(String[] args) {
    }

    @RequestMapping("/gggg")
    public String getSingleGraph(){
        return "singlegraph";
    }

    @RequestMapping("/demo")
    public String getDemoGraph(){
        return "demo";
    }


}
