package com.sudu.inas.service;


import com.sudu.inas.beans.Timenode;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.util.XStreamHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by J on  17-10-23.
 */

@Service
public class ReconsService {

    @Autowired
    HbaseDao hbaseDao;

    public boolean saveSingleTimenode(String objectId, Timenode timenode){
        String xml = XStreamHandle.toXml(timenode.getInfo());
        Boolean aBoolean = hbaseDao.insertData("Object", objectId, "timeline", timenode.getTimePoint(), xml, null);
        return aBoolean;
    }
}
