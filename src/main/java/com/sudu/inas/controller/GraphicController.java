package com.sudu.inas.controller;

import com.sudu.inas.beans.Edge;
import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.Node;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by J on  17-10-27.
 */

@Controller
public class GraphicController {

    @Autowired
    ObjectService objectService;

    public static int inerval = 20;


    @RequestMapping(value = "/graphic")
    public String showGraphy(){
      return "graphic";
    }

    @RequestMapping(value = "/graphic.do",method = RequestMethod.GET)
    public @ResponseBody Map<String,ArrayList> getGraphy(){
        String objectId = "LiBaiFQ82S8BT";
        Entity entity = null;
        try {
            entity = objectService.findObjectById(objectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Timenode> timeLine = entity.getTimeLine();
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        int nodeId = 1;
        int x = 10;
        int y = 10;
        Iterator<Timenode> iterator = timeLine.iterator();
        while (iterator.hasNext()){
            Timenode timenode = iterator.next();
            Node node = new Node();
            node.setId(String.valueOf(nodeId));
            nodeId++;
            node.setX(x);
            x += inerval;
            node.setY(y);
            y += inerval;
            node.setColor("red");
            node.setLabel(timenode.getTimePoint()+timenode.getInfo().toString());
            node.setSize(20);
            nodes.add(node);
        }
        if (nodes.size()!= 0){
            Iterator<Node> nodeIterator = nodes.iterator();
            nodeIterator.next();
            while (nodeIterator.hasNext()){
                Node node = nodeIterator.next();
                Edge edge = new Edge();
                edge.setTargetID(node.getId());
                edge.setSourceID(String.valueOf(Integer.parseInt(node.getId())-1));
                edges.add(edge);
            }
        }
        HashMap<String, ArrayList> map = new HashMap<>();
        map.put("nodes",nodes);
        map.put("edges",edges);

        return map;
    }
}
