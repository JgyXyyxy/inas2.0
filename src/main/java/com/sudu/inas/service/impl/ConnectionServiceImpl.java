package com.sudu.inas.service.impl;

import com.sudu.inas.beans.Connection;
import com.sudu.inas.beans.ConnectionList;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.service.ConnectionService;
import com.sudu.inas.util.HbaseModelUtil;
import com.sudu.inas.util.XStreamHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by J on  17-10-27.
 */

@Service("ConnectionService")
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    HbaseDao hbaseDao;

    /**
     * ok
     */
    @Override
    public ArrayList<Connection> findConncetionListByTimePoint(String objectId, String timePoint) throws Exception{

        String connListXml = hbaseDao.getDataFromQualifier(HbaseModelUtil.CONNTABLE, objectId, HbaseModelUtil.DEFAULT, timePoint);
        if ("".equals(connListXml)){
            return null;
        }
        else {
            ConnectionList connectionList = XStreamHandle.toBean(connListXml, ConnectionList.class);
            return connectionList.getConnections();
        }

    }

    @Override
    public Connection findConncetionByConnNo(String objectId, String timePoint, int connNo) throws Exception{
        ArrayList<Connection> connections = findConncetionListByTimePoint(objectId, timePoint);
        Iterator<Connection> iterator = connections.iterator();
        while (iterator.hasNext()){
            Connection connection = iterator.next();
            if (connNo == connection.getConnNo()){
                return connection;
            }

        }
        return null;
    }

    @Override
    public Connection findConnectionByIdAndTime(String objectId, String timePoint, String targetId, String targetTime) throws Exception {
        ArrayList<Connection> connections = findConncetionListByTimePoint(objectId, timePoint);
        Iterator<Connection> iterator = connections.iterator();
        while (iterator.hasNext()){
            Connection connection = iterator.next();
            if ((targetId.equals(connection.getConnObjectId())&&(targetTime.equals(connection.getConntimePoint())))){
                return connection;
            }

        }
        return null;
    }

    @Override
    public void editConnectionByConnNo(Connection connection, String objectId, String timePoint, int connNo)  {
        ArrayList<Connection> connections = null;
        try {
            connections = findConncetionListByTimePoint(objectId, timePoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<Connection> iterator = connections.iterator();
        while (iterator.hasNext()){
            Connection conn = iterator.next();
            if (connNo == conn.getConnNo()){
                conn.setConnObjectId(connection.getConnObjectId());
                conn.setConntimePoint(connection.getConntimePoint());
                conn.setInfluence(connection.getInfluence());
            }

        }
        delConnectionListByTimePoint(objectId,timePoint);
        addConnectionListByTimePoint(connections,objectId,timePoint);

    }

    @Override
    public void addConnectionListByTimePoint(List<Connection> connections, String objectId, String timePoint) {
        ArrayList<Connection> sortConnections = sortConnections((ArrayList<Connection>) connections);
        ConnectionList connectionList = new ConnectionList();
        connectionList.setConnections(sortConnections);
        hbaseDao.insertData(HbaseModelUtil.CONNTABLE,objectId,HbaseModelUtil.DEFAULT,timePoint,XStreamHandle.toXml(connectionList),null);
    }

    @Override
    public void addConnection(String objectId, String timePoint, Connection connection) {
        try {
            List<Connection> connections = findConncetionListByTimePoint(objectId, timePoint);
            if (null == connections){
                connections = new ArrayList();
            }
            connections.add(connection);
            delConnectionListByTimePoint(objectId,timePoint);
            addConnectionListByTimePoint(connections,objectId,timePoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void delConnectionListByTimePoint(String objectId, String timePoint) {
        hbaseDao.delColumnByQualifier(HbaseModelUtil.CONNTABLE,objectId,HbaseModelUtil.DEFAULT,timePoint);

    }

    @Override
    public void delConnectionByConnNo(String objectId, String timePoint, int connNo){

        ArrayList<Connection> connections = null;
        try {
            connections = findConncetionListByTimePoint(objectId, timePoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (connections != null) {
            connections.removeIf(conn -> connNo == conn.getConnNo());
        }
        delConnectionListByTimePoint(objectId,timePoint);
        addConnectionListByTimePoint(connections,objectId,timePoint);

    }

    private ArrayList<Connection> sortConnections(ArrayList<Connection> connections){
        int count = 0;
        Iterator<Connection> iterator = connections.iterator();
        while (iterator.hasNext()){
            Connection next = iterator.next();
            next.setConnNo(++count);
        }

        return connections;
    }

}
