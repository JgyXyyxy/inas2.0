package com.sudu.inas.service;

import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Timenode;

import java.util.List;

public interface TimelineService {

    List<Timenode> findTimelineByObjectId(String objectId);

    DetailedInfo findDetailinfoByTimepoint(String objectId, String timePoint);

    void insetTimenode(String objectId,Timenode timenode);

    void delTimenodeByTimepoint(String objectId,String timePoint);
}
