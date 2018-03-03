package com.sudu.inas.controller;

import com.sudu.inas.beans.Action;
import com.sudu.inas.beans.Entity;
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


import java.util.ArrayList;
import java.util.List;

@Controller
public class ExtractionController {

    @Autowired
    SRLService srlService;

    @Autowired
    ObjectService objectService;


    @RequestMapping(value = "/extract",method = RequestMethod.GET)
    public String extract(Model model){
        return "test";
    }

    @RequestMapping(value = "/extract.do",method = RequestMethod.POST)
    public @ResponseBody List<Action> getExtractResult(String rawinfo) throws InterruptedException {
        String[] strings = rawinfo.split("。");
        for (String b :strings) {
            b = b+"。";
            System.out.println(b);
            srlService.putSentence(b);
        }
        List<Action> actionList = srlService.start();
        for (Action action:actionList){
            System.out.println(action);
        }
        return actionList;
//        String randomNum = CommonUtil.genRandomNum();
//        ArrayList<Action> actionList = new ArrayList<>();
//        Action action1 = new Action("aaaa"+randomNum, "bbbb", "cccccc", "dddddd");
//        Action action2 = new Action("bbbb"+randomNum, "bbbb", "cccccc", "dddddd");
//        Action action3 = new Action("bbbb"+randomNum, "bbbb", "cccccc", "dddddd");
//        actionList.add(action1);
//        actionList.add(action2);
//        return actionList;

    }

    @RequestMapping("/extract/{objectId}")
    public String getDetailedInfo(@PathVariable String objectId, Model model) throws Exception {
        Entity entity = objectService.findObjectById(objectId);
        model.addAttribute("realName",entity.getRealName());
        model.addAttribute("objectId",objectId);
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
}
