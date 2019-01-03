package com.sudu.inas.service.impl;

import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.service.RawinfoService;
import com.sudu.inas.util.HbaseModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by J on  17-10-27.
 */

@Service("RawinfoService")
public class RawInfoServiceImpl implements RawinfoService{

    @Autowired
    HbaseDao hbaseDao;


    @Override
    public String findRealName(String objectId) {
        return hbaseDao.getDataFromQualifier(HbaseModelUtil.BASIC_TABLE,objectId,HbaseModelUtil.BASIC_RAW,HbaseModelUtil.COLUMN1);
    }

    @Override
    public String findRawText(String objectId) {

        return hbaseDao.getDataFromQualifier(HbaseModelUtil.BASIC_TABLE,objectId,HbaseModelUtil.BASIC_RAW,HbaseModelUtil.RAW_TEXT);
    }

    @Override
    public void addRealName(String realName, String objectId) {
        hbaseDao.insertData(HbaseModelUtil.BASICTABLE,objectId, HbaseModelUtil.CF1,HbaseModelUtil.COLUMN1,realName,null);

    }

    @Override
    public void insertRealName(String realName, String objectId) {
        hbaseDao.insertData(HbaseModelUtil.BASIC_TABLE,objectId,HbaseModelUtil.BASIC_RAW,HbaseModelUtil.COLUMN1,realName,null);
    }

    @Override
    public void addRawText(String rawText, String objectId) {
        hbaseDao.insertData(HbaseModelUtil.BASIC_TABLE,objectId, HbaseModelUtil.BASIC_RAW,HbaseModelUtil.RAW_TEXT,rawText,null);

    }
}
