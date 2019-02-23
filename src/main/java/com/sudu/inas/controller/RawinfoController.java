package com.sudu.inas.controller;


import com.sudu.inas.beans.*;
import com.sudu.inas.service.ObjectService;
import com.sudu.inas.service.RawinfoService;
import com.sudu.inas.service.TimelineService;
import com.sudu.inas.util.CommonUtil;
import com.sudu.inas.util.CrawlerUtil;

import com.sun.tools.corba.se.idl.StringGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by J on  17-11-6.
 */

@Controller
public class RawinfoController {

    @Autowired
    RawinfoService rawinfoService;

    @Autowired
    ObjectService objectService;

    @Autowired
    TimelineService timelineService;


    @RequestMapping(value = "/rawinfo.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, List> findRawInfo(String keyWord) throws Exception {
        CrawlerUtil crawlerUtil = new CrawlerUtil();
        List<String> linkList = crawlerUtil.crawlerLink(keyWord);
        ArrayList<String> paraList = new ArrayList<>();
        for (String link : linkList) {
            List<String> paras = crawlerUtil.parseBaike(link);
            StringBuilder builder = new StringBuilder();
            for (String para : paras) {
                builder.append(para);
                builder.append("\n");
            }
            paraList.add(builder.toString());
        }
        ArrayList<String> descriptionList = new ArrayList<>();
        List<Entity> entityList = objectService.findObjectsByPrefix(keyWord);
        for (Entity e : entityList) {
            String stringBuilder = e.getObjectId() +
                    ":" +
                    e.getRealName();
            descriptionList.add(stringBuilder);
        }
        HashMap<String, List> map = new HashMap<>();
        map.put("paras", paraList);
        map.put("entitylist", descriptionList);
        return map;
    }

    @RequestMapping(value = "/rawtext.do", method = RequestMethod.POST)
    public @ResponseBody
    List<String> getRowText(String objectId) {
        String rawText = rawinfoService.findRawText(objectId);
        ArrayList<String> strings = new ArrayList<>();
        strings.add(rawText);
        return strings;
    }


    @RequestMapping(value = "/saverawinfo.do", method = RequestMethod.POST)
    public @ResponseBody
    String saveRawinfo(String textarea1, String select) {
        if ("".equals(select)) {
            return "Please add new entity";
        }
        String[] strings = select.split(":");
        String objectId = strings[0];
        String descrip = strings[1];
        rawinfoService.addRawText(textarea1, objectId);
        return "OK";
    }

    @RequestMapping(value = "/saveraw.do", method = RequestMethod.POST)
    public @ResponseBody
    String saveRaw(String textarea1, String objectId) {
        rawinfoService.addRawinfo(objectId,textarea1);
        return "OK";
    }

    @RequestMapping(value = "/savenew.do", method = RequestMethod.POST)
    public @ResponseBody
    String saveNewWithRaw(String textarea1, String name, String description) {
        String objectId = name + CommonUtil.genRandomNum();
        rawinfoService.addRawText(textarea1, objectId);
        String realName = name + " " + description;
        rawinfoService.addRealName(realName, objectId);
        return "OK";
    }

    @RequestMapping(value = "/newentity.do", method = RequestMethod.POST)
    public @ResponseBody
    String saveNewWithReal(String name, String description) {
        String objectId = name + CommonUtil.genRandomNum();
        String realName = name + " " + description;
        rawinfoService.insertRealName(realName, objectId);
//        timelineService.insetTimenode(objectId, new Timenode("0000-00-00", new DetailedInfo("", realName, "")));
        UUID uuid=UUID.randomUUID();
        String  eventId = uuid.toString();
//        String eventId = str.replace("-", "");
        timelineService.insertEvent(objectId,new Event(eventId,objectId,"2050-01-01","",realName,""));
        return "OK";
    }

    public static void main(String[] args) {
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        System.out.println(str);
    }

    @RequestMapping("/rawinfo/{objectId}")
    public String getDetailedInfo(@PathVariable String objectId, Model model){

        String realname="";
        try {
            RealEntity entity = objectService.findEntityByIdFromEs(objectId);
            realname = entity.getRealName();
        } catch (Exception e) {
            
        }
        String rawinfo = rawinfoService.getRawinfo(objectId);
        model.addAttribute("objectId",objectId);
        model.addAttribute("realName",realname);
        model.addAttribute("rawinfo",rawinfo);
        return "rawinfo";
    }


}
