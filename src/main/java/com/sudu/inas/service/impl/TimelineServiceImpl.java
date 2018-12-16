package com.sudu.inas.service.impl;

import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Event;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.repository.EventRepository;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.repository.RelevanceRepository;
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

    @Autowired
    EventRepository eventRepository;

    @Autowired
    RelevanceRepository relevanceRepository;




    @Override
    public List<Timenode> findTimelineByObjectId(String objectId) {
        return null;
    }

    @Override
    public Event findEventByEventId(String eventId) {
        return eventRepository.queryEventByEventId(eventId);
    }


    @Override
    public DetailedInfo findDetailinfoByTimepoint(String objectId, String timePoint) {
        String info = hbaseDao.getDataFromQualifier(HbaseModelUtil.BASICTABLE, objectId, HbaseModelUtil.CF2, timePoint);
        if ("".equals(info)){
            return null;
        }
        DetailedInfo detailedInfo = XStreamHandle.toBean(info, DetailedInfo.class);
        return detailedInfo;
    }


    @Override
    public void insetTimenode(String objectId, Timenode timenode) {
        String xml = XStreamHandle.toXml(timenode.getInfo());
        Boolean aBoolean = hbaseDao.insertData("Object", objectId, "timeline", timenode.getTimePoint(), xml, null);
    }

    @Override
    public void insertEvent(String objectId, Event event) {
        hbaseDao.insertData(HbaseModelUtil.BASIC_TABLE,objectId,HbaseModelUtil.BASIC_EVENT,event.getTs(),event.getEventId(),null);
        hbaseDao.insertData(HbaseModelUtil.EVENTS_TABLE,event.getEventId(),HbaseModelUtil.EVENTS_PARAMS,"ts",event.getTs(),null);
        hbaseDao.insertData(HbaseModelUtil.EVENTS_TABLE,event.getEventId(),HbaseModelUtil.EVENTS_PARAMS,"site",event.getSite(),null);
        hbaseDao.insertData(HbaseModelUtil.EVENTS_TABLE,event.getEventId(),HbaseModelUtil.EVENTS_PARAMS,"details",event.getDetails(),null);
        hbaseDao.insertData(HbaseModelUtil.EVENTS_TABLE,event.getEventId(),HbaseModelUtil.EVENTS_PARAMS,"affect",event.getAffect(),null);
        eventRepository.deleteEventByEventId(event.getEventId());
        eventRepository.save(event);
    }

    @Override
    public void delTimenodeByTimepoint(String objectId, String timePoint) {
        hbaseDao.delColumnByQualifier(HbaseModelUtil.BASICTABLE,objectId,HbaseModelUtil.CF2,timePoint);
    }

    @Override
    public void delEvent(Event event) {
        hbaseDao.delColumnByQualifier(HbaseModelUtil.BASIC_TABLE,event.getObjectId(),HbaseModelUtil.BASIC_EVENT,event.getTs());
        eventRepository.deleteEventByEventId(event.getEventId());
        relevanceRepository.deleteRelevanceBySourceEventId(event.getEventId());
        relevanceRepository.deleteRelevanceByTargetEventId(event.getEventId());
    }
}
