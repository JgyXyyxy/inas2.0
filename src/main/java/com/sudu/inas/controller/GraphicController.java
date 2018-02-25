package com.sudu.inas.controller;

import com.sudu.inas.beans.*;
import com.sudu.inas.service.ConnectionService;
import com.sudu.inas.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by J on  17-10-27.
 */

@Controller
public class GraphicController {

    @Autowired
    ObjectService objectService;

    @Autowired
    ConnectionService connectionService;

    public static int interval = 40;


    @RequestMapping(value = "/graph.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ArrayList> getGraphy(String objectId) throws Exception {
//        String objectId = "LiBaiFQ82S8BT";
//        String objectId = "Boston";
        Entity entity = null;
        int tag = 0;
        int xBegin = 20;
        int yBegin = 20;
        entity = objectService.findObjectById(objectId);
        if (null == entity){
            return null;
        }
        ArrayList<Timenode> timeLine = entity.getTimeLine();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<String> connectedIds = new ArrayList<>();

        for (Timenode timenode : timeLine) {
            Node node = transTimenodeToNode(objectId, timenode);
            node.setColor("red");
            node.setX(xBegin + tag * interval);
            node.setY(yBegin);
            tag++;
            nodes.add(node);
            List<Connection> conncetionListByTimePoint = connectionService.findConncetionListByTimePoint(objectId, timenode.getTimePoint());
            if (conncetionListByTimePoint != null) {
                for (Connection connection : conncetionListByTimePoint) {
                    Edge edge = transNodesToEdge(objectId, timenode.getTimePoint(), connection.getConnObjectId(), connection.getConntimePoint());
                    edges.add(edge);
                    if (!connectedIds.contains(connection.getConnObjectId())) {
                        Entity objectById = objectService.findObjectById(connection.getConnObjectId());
                        ArrayList<Timenode> timeLine1 = objectById.getTimeLine();
                        int num = 0;
                        for (Timenode timenode1 : timeLine1) {
                            Node node1 = transTimenodeToNode(connection.getConnObjectId(), timenode1);
                            node1.setColor("blue");
                            node1.setX(xBegin + num * interval / 2);
                            int objectNum = connectedIds.size() + 1;
                            node1.setY(yBegin + interval * objectNum / 2);
                            nodes.add(node1);
                            num++;
                        }
                        connectedIds.add(connection.getConnObjectId());
                    }

                }
            }

        }
        connectedIds.add(objectId);
        for (String id : connectedIds) {
            Entity objectById = objectService.findObjectById(id);
            ArrayList<Timenode> line = objectById.getTimeLine();
            Iterator<Timenode> iterator = line.iterator();
            Timenode next = iterator.next();
            String tmp = next.getTimePoint();
            while (iterator.hasNext()) {
                Timenode nextNode = iterator.next();
                Edge edge = transNodesToEdge(id, tmp, id, nextNode.getTimePoint());
                edges.add(edge);
                tmp = nextNode.getTimePoint();
            }

        }
        HashMap<String, ArrayList> map = new HashMap<>();
        map.put("nodes", nodes);
        map.put("edges", edges);
        return map;

    }

    public Node transTimenodeToNode(String objectId, Timenode timenode) {
        Node node = new Node();
        node.setId(objectId + timenode.getTimePoint());
        node.setLabel(objectId + timenode.getTimePoint() + timenode.getInfo().toString());
        node.setSize(50);
        return node;
    }

    public Edge transNodesToEdge(String sourceId, String sourceTimepoint, String targetId, String targetTimepoint) {
        Edge edge = new Edge();
        edge.setSourceID(sourceId + sourceTimepoint);
        edge.setTargetID(targetId + targetTimepoint);
        return edge;
    }

    @RequestMapping(value = "/graphtest.do",method = RequestMethod.POST)
    public @ResponseBody  Map<String, ArrayList> getGraph(String objectId){
        Node node1 = new Node(10,10,"1","aaa",50,"red");
        Node node2 = new Node(10,20,"2","aaa",50,"red");
        Edge edge = new Edge("1","2");
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(edge);
        HashMap<String, ArrayList> map = new HashMap<>();
        map.put("nodes", nodes);
        map.put("edges", edges);
        return map;
    }
}
