package com.sudu.inas.service;

import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Event;
import com.sudu.inas.beans.Timenode;

import java.util.List;

/**
 * Created by J on  17-10-27.
 */

public interface TimelineService {

    List<Timenode> findTimelineByObjectId(String objectId);

    Event findEventByEventId(String eventId);

    DetailedInfo findDetailinfoByTimepoint(String objectId, String timePoint) throws Exception;

    void insetTimenode(String objectId,Timenode timenode);

    void insertEvent(String objectId, Event event);

    void delTimenodeByTimepoint(String objectId,String timePoint);

    void delEventByEventId(String eventId);

}
