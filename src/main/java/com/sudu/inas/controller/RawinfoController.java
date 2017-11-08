package com.sudu.inas.controller;


import com.sudu.inas.beans.Entity;
import com.sudu.inas.service.ObjectService;
import com.sudu.inas.service.RawinfoService;
import com.sudu.inas.util.CommonUtil;
import com.sudu.inas.util.CrawlerUtil;

import com.sun.tools.corba.se.idl.StringGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J on  17-11-6.
 */

@Controller
public class RawinfoController {

    @Autowired
    RawinfoService rawinfoService;

    @Autowired
    ObjectService objectService;


    @RequestMapping(value = "/rawinfo.do",method = RequestMethod.POST)
    public @ResponseBody
    Map<String,List> findRawInfo(String keyWord) throws Exception {
        CrawlerUtil crawlerUtil = new CrawlerUtil();
        List<String> linkList = crawlerUtil.crawlerLink(keyWord);
        ArrayList<String> paraList = new ArrayList<>();
        for (String link:linkList) {
            List<String> paras = crawlerUtil.parseBaike(link);
            StringBuilder builder = new StringBuilder();
            for (String para:paras) {
                builder.append(para);
                builder.append("\n");
            }
            paraList.add(builder.toString());
        }
        ArrayList<String> descriptionList = new ArrayList<>();
        List<Entity> entityList = objectService.findObjectsByPrefix(keyWord);
        for (Entity e:entityList) {
            String stringBuilder = e.getObjectId() +
                    ":" +
                    e.getRealName();
            descriptionList.add(stringBuilder);
        }
        HashMap<String, List> map = new HashMap<>();
        map.put("paras",paraList);
        map.put("entitylist",descriptionList);
        return map;
    }

    @RequestMapping(value = "/rawtext.do",method = RequestMethod.POST)
    public @ResponseBody List<String> getRowText(String objectId){
        String rawText = rawinfoService.findRawText(objectId);
        ArrayList<String> strings = new ArrayList<>();
        strings.add(rawText);
        return strings;
    }


    @RequestMapping(value = "/saverawinfo.do",method = RequestMethod.POST)
    public @ResponseBody String saveRawinfo(String textarea1,String select){
        String[] strings = select.split(":");
        String objectId = strings[0];
        String descrip = strings[1];
        rawinfoService.addRawText(textarea1,objectId);
        return "OK";
    }

    @RequestMapping(value = "/savenew.do",method = RequestMethod.POST)
    public @ResponseBody String saveNew(String textarea1,String name,String description){
        String objectId = name + CommonUtil.genRandomNum();
        rawinfoService.addRawText(textarea1,objectId);
        String realName = name+" "+description;
        rawinfoService.addRealName(realName,objectId);
        return "OK";
    }








}
