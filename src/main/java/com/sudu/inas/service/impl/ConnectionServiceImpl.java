package com.sudu.inas.service.impl;

import com.sudu.inas.beans.Connection;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.service.ConnectionService;
import com.sudu.inas.util.HbaseModelUtil;
import com.sudu.inas.util.XStreamHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service("ConnectionService")
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    HbaseDao hbaseDao;


    @Override
    public List<Connection> findConncetionListByTimePoint(String objectId, String timePoint) {
        return null;
    }


    @Override
    public void addConnectionByTimePoint(Connection connection, String objectId, String timePoint) {
        String xml = XStreamHandle.toXml(connection);
        hbaseDao.insertData(HbaseModelUtil.CONNTABLE,objectId,HbaseModelUtil.DEFAULT,timePoint,xml,null);
    }

    @Override
    public void delConnectionByTimePoint(String objectId, String timePoint) {

    }
}
