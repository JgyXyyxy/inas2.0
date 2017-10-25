package com.sudu.inas.controller;


import com.sudu.inas.repository.HbaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by J on  17-10-23.
 */

@Controller
public class testController {

    @Autowired
    HbaseDao hbaseDao;

    @RequestMapping("/test")
    public @ResponseBody String testGetvalue(){
        return hbaseDao.getDataFromQualifier("Object","LiBai","rawinfo","realname");
    }

}
