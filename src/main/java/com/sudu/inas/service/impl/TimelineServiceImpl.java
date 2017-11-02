package com.sudu.inas.service.impl;

import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.service.TimelineService;
import com.sudu.inas.util.HbaseModelUtil;
import com.sudu.inas.util.XStreamHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by J on  17-10-27.
 */

@Service("TimelineService")
public class TimelineServiceImpl implements TimelineService {

    @Autowired
    HbaseDao hbaseDao;


    @Override
    public List<Timenode> findTimelineByObjectId(String objectId) {
        return null;
    }

    @Override
    public DetailedInfo findDetailinfoByTimepoint(String objectId, String timePoint) {
        String info = hbaseDao.getDataFromQualifier(HbaseModelUtil.BASICTABLE, objectId, HbaseModelUtil.CF2, timePoint);
        DetailedInfo detailedInfo = XStreamHandle.toBean(info, DetailedInfo.class);
        return detailedInfo;
    }


    @Override
    public void insetTimenode(String objectId, Timenode timenode) {
        String xml = XStreamHandle.toXml(timenode.getInfo());
        Boolean aBoolean = hbaseDao.insertData("Object", objectId, "timeline", timenode.getTimePoint(), xml, null);
    }

    @Override
    public void delTimenodeByTimepoint(String objectId, String timePoint) {
        hbaseDao.delColumnByQualifier(HbaseModelUtil.BASICTABLE,objectId,HbaseModelUtil.CF2,timePoint);
    }
}
