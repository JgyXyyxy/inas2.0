package com.sudu.inas.service;

import com.sudu.inas.beans.Connection;

import java.util.List;

public interface ConnectionService {


    List<Connection> findConncetionListByTimePoint(String objectId, String timePoint);

    void addConnectionByTimePoint(Connection connection,String objectId, String timePoint);

    void delConnectionByTimePoint(String objectId,String timePoint);
}
