package com.sudu.inas.service.impl;

import com.sudu.inas.beans.HbaseModel;
import com.sudu.inas.beans.Tuple;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.service.ParamService;
import com.sudu.inas.util.HbaseModelUtil;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("ParamService")
public class ParamServiceImpl implements ParamService {

    @Autowired
    HbaseDao hbaseDao;

    @Override
    public void delParam(String objectId, String key) {
        hbaseDao.delColumnByQualifier(HbaseModelUtil.BASIC_TABLE,objectId,HbaseModelUtil.BASIC_RAW,key);
    }

    @Override
    public void insertParam(String objectId, String key, String value) {
        hbaseDao.insertData(HbaseModelUtil.BASIC_TABLE,objectId,HbaseModelUtil.BASIC_RAW,key,value,null);
    }

    @Override
    public List<Tuple> getAllParams(String objectId) {
        Result result = hbaseDao.getDataFromRowkey(HbaseModelUtil.BASIC_TABLE, objectId);
        ArrayList<Tuple> params = new ArrayList<>();
        for (KeyValue kv :result.list()){
            HbaseModel hbaseModel = HbaseModelUtil.kvToHbaseModel(kv);
            packParam(hbaseModel,params);
        }
        return params;
    }

    private void packParam(HbaseModel hbaseModel, ArrayList<Tuple> params) {
        if (HbaseModelUtil.BASIC_RAW.equals(hbaseModel.getFamilyName())){
            Tuple tuple = new Tuple(hbaseModel.getQualifier(), hbaseModel.getValue());
            params.add(tuple);
        }
    }
}
