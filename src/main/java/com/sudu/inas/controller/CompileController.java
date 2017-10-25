package com.sudu.inas.controller;


import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on  17-10-23.
 */

@Controller
public class CompileController {

    @Autowired
    EditorService editorService;

    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public String getSimilarKey(String keyBegin, Model model){
        List<Entity> entityList = editorService.findObjectByPre(keyBegin);
        model.addAttribute("entities",entityList);
        return "searchresult";

    }

    @RequestMapping("/search/{objectId}")
    public String getDetailedInfo(@PathVariable String objectId, Model model){
        Entity objectById = editorService.findObjectById(objectId);
        model.addAttribute("objectId",objectId);
        ArrayList<Timenode> timeLine = objectById.getTimeLine();
        model.addAttribute("timeline",timeLine);
        return "showdetailed";

    }

    @RequestMapping("/entity/{idPlusQua}")
    public String getTimeNode(@PathVariable String idPlusQua, Model model)throws Exception{
        String[] strings = idPlusQua.split("plus");
        String objectId = strings[0];
        String qualifier = strings[1];
        DetailedInfo detailedInfo = editorService.findDetailbyQualifier(objectId, qualifier);
        model.addAttribute("detail",detailedInfo);
        return "showtimenode";
    }


}
