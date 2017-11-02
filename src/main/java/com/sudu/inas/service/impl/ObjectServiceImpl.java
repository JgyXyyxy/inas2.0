package com.sudu.inas.service.impl;

import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.HbaseModel;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.service.ObjectService;
import com.sudu.inas.util.HbaseModelUtil;
import com.sudu.inas.util.XStreamHandle;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by J on  17-10-27.
 */

@Service("ObjectService")
public class ObjectServiceImpl implements ObjectService {

    @Autowired
    HbaseDao hbaseDao;


    @Override
    public Entity findObjectById(String objectId) {
        Result result = hbaseDao.getDataFromRowkey("Object", objectId);
        Entity entity = new Entity();
        entity.setObjectId(objectId);
        for (KeyValue kv:result.list()){
            HbaseModel hbaseModel = HbaseModelUtil.kvToHbaseModel(kv);
            Entity entity1 = insertSingleModel(entity, hbaseModel);
            entity = entity1;
        }
        return entity;
    }

    @Override
    public List<Entity> findObjectsByPrefix(String prefix) {

        List<Result> objects = hbaseDao.getDataWithSameBegining("Object", prefix);
        Iterator<Result> iterator = objects.iterator();
        HashMap<String, Entity> entityHashMap = new HashMap<>();
        while (iterator.hasNext()) {
            Result result = iterator.next();
            for (KeyValue kv : result.list()) {
                HbaseModel hbaseModel = HbaseModelUtil.kvToHbaseModel(kv);
                if (entityHashMap.containsKey(hbaseModel.getRow())) {
                    Entity entity = entityHashMap.get(hbaseModel.getRow());
                    Entity entity1 = insertSingleModel(entity, hbaseModel);
                    entityHashMap.remove(hbaseModel.getRow());
                    entityHashMap.put(hbaseModel.getRow(), entity1);

                } else {
                    Entity newEntity = new Entity();
                    newEntity.setObjectId(hbaseModel.getRow());
                    Entity entity2 = insertSingleModel(newEntity, hbaseModel);
                    entityHashMap.put(hbaseModel.getRow(), entity2);
                }
            }
        }

        ArrayList<Entity> entities = new ArrayList<>();
        Iterator<String> iterator1 = entityHashMap.keySet().iterator();
        while (iterator1.hasNext()){
            String s = iterator1.next();
            Entity entity = entityHashMap.get(s);
            entities.add(entity);
        }
        return entities;
    }



    public Entity insertSingleModel(Entity entity,HbaseModel hbaseModel){
        ArrayList<Timenode> timeLine = entity.getTimeLine();
        if (HbaseModelUtil.CF2.equals(hbaseModel.getFamilyName())) {
            Timenode timenode = new Timenode();
            timenode.setTimePoint(hbaseModel.getQualifier());
            timenode.setInfo(XStreamHandle.toBean(hbaseModel.getValue(), DetailedInfo.class));
            timeLine.add(timenode);
        } else{
            if (HbaseModelUtil.COLUMN2.equals(hbaseModel.getQualifier())){
                entity.setRawInfo(hbaseModel.getValue());
            }else {
                entity.setRealName(hbaseModel.getValue());
            }
        }
        return entity;
    }

}
