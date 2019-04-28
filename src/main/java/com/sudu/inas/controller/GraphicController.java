package com.sudu.inas.controller;

import com.sudu.inas.beans.*;
import com.sudu.inas.service.ConnectionService;
import com.sudu.inas.service.ObjectService;
import com.sudu.inas.service.RelevanceService;
import com.sudu.inas.service.TimelineService;
import com.sudu.inas.util.CommonUtil;
import com.sun.tools.javap.TypeAnnotationWriter;
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

    @Autowired
    RelevanceService relevanceService;

    @Autowired
    TimelineService timelineService;

    public static int interval = 10;
    public static int xBegin = 50;
    public static int yBegin = 50;


    @RequestMapping(value = "/graph.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ArrayList> getGraphy(String objectId) throws Exception {

        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<String> reletedIds = new ArrayList<>();
        int num = 0;


//            List<Connection> conncetionListByTimePoint = connectionService.findConncetionListByTimePoint(objectId, timenode.getTimePoint());
//            if (conncetionListByTimePoint != null) {
//                for (Connection connection : conncetionListByTimePoint) {
//                    Edge edge = transNodesToEdge(objectId, timenode.getTimePoint(), connection.getConnObjectId(), connection.getConntimePoint());
//                    edges.add(edge);
//                    if (!connectedIds.contains(connection.getConnObjectId())) {
//                        Entity objectById = objectService.findObjectById(connection.getConnObjectId());
//                        ArrayList<Timenode> timeLine1 = objectById.getTimeLine();
//                        int num = 0;
//                        for (Timenode timenode1 : timeLine1) {
//                            Node node1 = transTimenodeToNode(connection.getConnObjectId(), timenode1);
//                            node1.setColor("blue");
//                            node1.setX(xBegin + num * interval / 2);
//                            int objectNum = connectedIds.size() + 1;
//                            node1.setY(yBegin + interval * objectNum / 2);
//                            nodes.add(node1);
//                            num++;
//                        }
//                        connectedIds.add(connection.getConnObjectId());
//                    }
//
//                }
//            }
//
//        }
//        connectedIds.add(objectId);
//        for (String id : connectedIds) {
//            Entity objectById = objectService.findObjectById(id);
//            ArrayList<Timenode> line = objectById.getTimeLine();
//            Iterator<Timenode> iterator = line.iterator();
//            Timenode next = iterator.next();
//            String tmp = next.getTimePoint();
//            while (iterator.hasNext()) {
//                Timenode nextNode = iterator.next();
//                Edge edge = transNodesToEdge(id, tmp, id, nextNode.getTimePoint());
//                edges.add(edge);
//                tmp = nextNode.getTimePoint();
//            }
//

        List<Relevance> activeRelevances = relevanceService.getActiveRelevancesForEntity(objectId);
        List<Relevance> passiveRelevances = relevanceService.getPassiveRelevancesForEntity(objectId);
        ArrayList<Relevance> relevances = new ArrayList<>();

        if (activeRelevances.size() != 0) {
            relevances.addAll(activeRelevances);
        }
        if (passiveRelevances.size() != 0) {
            relevances.addAll(passiveRelevances);
        }

        for (Relevance relevance : relevances) {
            String sourcePointId = relevance.getSourceEntityId() + ":" + relevance.getSourceEventId();
            String targetPointId = relevance.getTargetEntityId() + ":" + relevance.getTargetEventId();
            Edge edge = new Edge(sourcePointId, targetPointId, relevance.getDescription());
            edges.add(edge);
            if (relevance.getSourceEntityId().equals(objectId) && (!relevance.getTargetEntityId().equals(objectId))) {
                if (!reletedIds.contains(relevance.getTargetEntityId())) {
                    reletedIds.add(relevance.getTargetEntityId());
                }
            }
            if (relevance.getTargetEntityId().equals(objectId) && (!relevance.getSourceEntityId().equals(objectId))) {
                if (!reletedIds.contains(relevance.getSourceEntityId())) {
                    reletedIds.add(relevance.getSourceEntityId());
                }
            }
        }

        graphFromEntityId(objectId, nodes, edges, true, num);
        num++;
        for (String id : reletedIds) {
            graphFromEntityId(id, nodes, edges, false, num);
            num++;
        }


        HashMap<String, ArrayList> map = new HashMap<>();

        map.put("nodes", nodes);
        map.put("edges", edges);
        return map;

    }

    public Node transEvent2Node(int change, String objectId, Event event) {
        Node node = new Node();
        node.setId(objectId + ":" + event.getEventId());

        if (!(change % 2 == 0)) {
            node.setLabel("\n" + event.pack());
        }else {
            node.setLabel(event.pack());
        }
        node.setSize(20);
        return node;
    }

    public Edge transNodesToEdge(String sourceId, String sourceTimepoint, String targetId, String targetTimepoint) {
        Edge edge = new Edge();
        edge.setSourceID(sourceId + sourceTimepoint);
        edge.setTargetID(targetId + targetTimepoint);
        return edge;
    }

    public void graphFromEntityId(String objectId, List<Node> nodes, List<Edge> edges, boolean tag, int num) {

        RealEntity realEntity = null;
        try {
            realEntity = objectService.findEntityByIdFromEs(objectId);
        } catch (Exception e) {
            System.out.println("get object failed");
        }
        if (null == realEntity) {
            return;
        }
        ArrayList<Event> events = realEntity.getEvents();
        int i = 0;
        Node exNode = null;
        Node firstNode = null;
        Node nameNode = new Node();
        int no =2;
        for (Event event : events) {
            if (!"2050-01-01".equals(event.getTs())) {
                Node node = transEvent2Node(no, objectId, event);
                no++;
                if (tag) {
                    node.setColor("red");
                } else {
                    node.setColor("blue");
                }
                node.setX(xBegin + i * interval);
                node.setY(yBegin + num * interval);
                nodes.add(node);
                i++;

                if (i == 1) {
                    exNode = node;
                    firstNode = node;
                } else {
                    edges.add(new Edge(exNode.getId(), node.getId(), ""));
                    exNode = node;
                }
            } else {

                nameNode.setId(objectId + ":" + event.getEventId());
                nameNode.setLabel("\n" + event.getDetails());
                nameNode.setSize(20);
                if (tag) {
                    nameNode.setColor("red");
                } else {
                    nameNode.setColor("blue");
                }
                nameNode.setX(xBegin - interval);
                nameNode.setY(yBegin + num * interval);
                nodes.add(nameNode);
            }

            if (null != firstNode) {
                edges.add(new Edge(nameNode.getId(), firstNode.getId(), ""));
            }


        }
    }

    @RequestMapping(value = "/graphforevent.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ArrayList> getForceGraphy(String objectId) throws Exception {
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<String> reletedIds = new ArrayList<>();

        List<Relevance> activeRelevances = relevanceService.getActiveRelevancesForEntity(objectId);
        List<Relevance> passiveRelevances = relevanceService.getPassiveRelevancesForEntity(objectId);
        ArrayList<Relevance> relevances = new ArrayList<>();

        if (activeRelevances.size() != 0) {
            relevances.addAll(activeRelevances);
        }
        if (passiveRelevances.size() != 0) {
            relevances.addAll(passiveRelevances);
        }

        for (Relevance relevance : relevances) {
            String sourcePointId = relevance.getSourceEntityId() + ":" + relevance.getSourceEventId();
            String targetPointId = relevance.getTargetEntityId() + ":" + relevance.getTargetEventId();
            Edge edge = new Edge(sourcePointId, targetPointId, relevance.getDescription());
            edges.add(edge);
            if (relevance.getSourceEntityId().equals(objectId) && (!relevance.getTargetEntityId().equals(objectId))) {
                if (!reletedIds.contains(relevance.getTargetEntityId())) {
                    reletedIds.add(relevance.getTargetEntityId());
                    categories.add(new Category(relevance.getTargetEntityId()));
                }
            }
            if (relevance.getTargetEntityId().equals(objectId) && (!relevance.getSourceEntityId().equals(objectId))) {
                if (!reletedIds.contains(relevance.getSourceEntityId())) {
                    reletedIds.add(relevance.getSourceEntityId());
                    categories.add(new Category(relevance.getSourceEntityId()));

                }
            }
        }


        RealEntity realEntity = objectService.findEntityByIdFromEs(objectId);
        if (null == realEntity) {
            return null;
        }
        ArrayList<Event> events = realEntity.getEvents();
        categories.add(new Category(objectId));
        for (Event event : events) {
            if (!event.getTs().equals("2050-01-01")) {
                Node node = new Node();
                node.setSize(20);
                node.setSerial(reletedIds.size());
                node.setLabel(event.pack());
                node.setId(objectId + ":" + event.getEventId());
                nodes.add(node);
            }
        }

        if (nodes.size() > 1) {
            for (int i = 0; i < nodes.size() - 1; i++) {
                Node sourceNode = nodes.get(i);
                String source = sourceNode.getId();
                Node targetNode = nodes.get(i + 1);
                String target = targetNode.getId();
                Edge edge = new Edge(source, target, "");
                edges.add(edge);
            }
        }

        for (Relevance relevance : relevances) {
            if (!relevance.getTargetEntityId().equals(objectId)) {
                Event event = timelineService.findEventByEventId(relevance.getTargetEventId());
                Node node = new Node();
                node.setSize(20);
                node.setSerial(reletedIds.indexOf(relevance.getTargetEntityId()));
                node.setLabel(event.pack());
                node.setId(relevance.getTargetEntityId() + ":" + event.getEventId());
                if (!nodes.contains(node)) {
                    nodes.add(node);
                }

            }
            if (!relevance.getSourceEntityId().equals(objectId)) {
                Event event = timelineService.findEventByEventId(relevance.getSourceEventId());
                Node node = new Node();
                node.setSize(20);
                node.setSerial(reletedIds.indexOf(relevance.getSourceEntityId()));
                node.setLabel(event.pack());
                node.setId(relevance.getSourceEntityId() + ":" + event.getEventId());
                if (!nodes.contains(node)) {
                    nodes.add(node);
                }

            }
        }

        HashMap<String, ArrayList> map = new HashMap<>();
        map.put("nodes", nodes);
        map.put("edges", edges);
        map.put("categories", categories);
        return map;
    }

}
