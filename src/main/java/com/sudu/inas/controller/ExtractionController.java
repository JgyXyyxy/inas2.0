package com.sudu.inas.controller;

import com.sudu.inas.beans.Action;
import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.RealEntity;
import com.sudu.inas.beans.SyntaxResult;
import com.sudu.inas.service.LtpService;
import com.sudu.inas.service.ObjectService;
import com.sudu.inas.service.SRLService;
import com.sudu.inas.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ExtractionController {

    @Autowired
    SRLService srlService;

    @Autowired
    ObjectService objectService;

    @Autowired
    LtpService ltpService;


    @RequestMapping(value = "/extract", method = RequestMethod.GET)
    public String extract(Model model) {
        return "test";
    }

    @RequestMapping(value = "/extract.do", method = RequestMethod.POST)
    public @ResponseBody
    List<Action> getExtractResult(String rawinfo) throws InterruptedException {

        ArrayList<Action> actionList = new ArrayList<>();
        System.out.println(rawinfo);
        String[] split = rawinfo.split("。");

        for (String b : split) {
            b = b + "。";
            System.out.println(b);
            SyntaxResult ltpResult = ltpService.getLtpResult(b);
            try {
                List<Action> dotrans = ltpResult.dotrans();
                actionList.addAll(dotrans);
            } catch (ParseException e) {
                System.out.println("tans error");
                e.printStackTrace();
            }
        }
        for (Action action : actionList) {
            System.out.println(action);
        }
        return actionList;

    }

    @RequestMapping("/extract/{objectId}")
    public String getDetailedInfo(@PathVariable String objectId, Model model) throws Exception {
//        Entity entity = objectService.findObjectById(objectId);
        RealEntity entity = objectService.findEntityById(objectId);
        model.addAttribute("realName", entity.getRealName());
        model.addAttribute("objectId", objectId);
//        String[] strings = rawInfo.split("。");
//        for (String b :strings) {
//            b = b+"。";
//            System.out.println(b);
//            srlService.putSentence(b);
//        }
//        List<Action> actionList = srlService.start();
//        for (Action action:actionList){
//            System.out.println(action);
//        }
        return "extractionresult";
    }

    public static void main(String[] args) {
        String a = "奥巴马，1961年8月4日出生，美国民主党籍政治家，第44任美国总统，为美国历史上第一位非洲裔总统。1991年，奥巴马以优等生荣誉从哈佛法学院毕业。2007年2月10日，奥巴马宣布参加2008年美国总统选举。2008年11月4日，奥巴马正式当选美国总统。";
        String[] split = a.split("。");
        for (String b:split){
            System.out.println(b);
        }
    }


}
