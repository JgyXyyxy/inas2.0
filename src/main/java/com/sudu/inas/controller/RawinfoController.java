package com.sudu.inas.controller;


import com.sudu.inas.util.CrawlerUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on  17-11-6.
 */

@Controller
public class RawinfoController {


    @RequestMapping(value = "/rawinfo.do",method = RequestMethod.POST)
    public @ResponseBody List<String> findRawInfo(String keyWord, Model model) throws IOException {
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
        return paraList;
    }

//    @RequestMapping(value = "rawinfo",method = RequestMethod.POST)
//    public String toRawInfo(String )





}
