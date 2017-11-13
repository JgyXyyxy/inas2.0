package com.sudu.inas.service;

import com.sudu.inas.beans.Connection;

import java.util.List;

/**
 * Created by J on  17-10-27.
 */

public interface ConnectionService {


    List<Connection> findConncetionListByTimePoint(String objectId, String timePoint) throws Exception;

    Connection findConncetionByConnNo(String objectId, String timePoint,int connNo) throws Exception;

    Connection findConnectionByIdAndTime(String objectId,String timePoint,String targetId,String targetTime) throws Exception;

    void editConnectionByConnNo(Connection connection,String objectId, String timePoint,int connNo);

    void addConnectionListByTimePoint(List<Connection> connections,String objectId, String timePoint);

    void addConnection(String objectId,String timePoint,Connection connection);

    void delConnectionListByTimePoint(String objectId,String timePoint);

    void delConnectionByConnNo(String objectId,String timePoint,int connNo);
}
