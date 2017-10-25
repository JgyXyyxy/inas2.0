package com.sudu.inas.service;

import com.sudu.inas.beans.DetailedInfo;
import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.HbaseModel;
import com.sudu.inas.beans.Timenode;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.util.XStreamHandle;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

/**
 * Created by J on  17-10-24.
 */

@Service
public class EditorService {

    @Autowired
    HbaseDao hbaseDao;

    public static String table = "Object";
    public static String cf1 = "rawinfo";
    public static String cf2 = "timeline";

    public Entity findObjectById(String objectId) {
        Result result = hbaseDao.getDataFromRowkey("Object", objectId);
        Entity entity = new Entity();
        entity.setObjectId(objectId);
        for (KeyValue kv:result.list()){
            HbaseModel hbaseModel = kvToHbaseModel(kv);
            Entity entity1 = insertSingleModel(entity, hbaseModel);
            entity = entity1;
        }
        return entity;
    }

    public  List<Entity> findObjectByPre(String pre) throws NullPointerException{

        List<Result> objects = hbaseDao.getDataWithSameBegining("Object", pre);
        Iterator<Result> iterator = objects.iterator();
        HashMap<String, Entity> entityHashMap = new HashMap<>();
        while (iterator.hasNext()) {
            Result result = iterator.next();
            for (KeyValue kv : result.list()) {
                HbaseModel hbaseModel = kvToHbaseModel(kv);
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

    public HbaseModel kvToHbaseModel(KeyValue kv) {

        HbaseModel hbaseModel = new HbaseModel();
        hbaseModel.setRow(Bytes.toString(kv.getRow()));
        hbaseModel.setFamilyName(Bytes.toString(kv.getFamily()));
        hbaseModel.setQualifier(Bytes.toString(kv.getQualifier()));
        hbaseModel.setValue(Bytes.toString(kv.getValue()));
        return hbaseModel;
    }

    public Entity insertSingleModel(Entity entity,HbaseModel hbaseModel){
        ArrayList<Timenode> timeLine = entity.getTimeLine();
        if (cf2.equals(hbaseModel.getFamilyName())) {
            Timenode timenode = new Timenode();
            timenode.setTimePoint(hbaseModel.getQualifier());
            timenode.setInfo(XStreamHandle.toBean(hbaseModel.getValue(), DetailedInfo.class));
            timeLine.add(timenode);
        } else{
            if ("rawtext".equals(hbaseModel.getQualifier())){
                entity.setRawInfo(hbaseModel.getValue());
            }else {
                entity.setRealName(hbaseModel.getValue());
            }
        }
        return entity;
    }

    public DetailedInfo findDetailbyQualifier(String rowKey,String qualifier){
        String info = hbaseDao.getDataFromQualifier(table, rowKey, cf2, qualifier);
        DetailedInfo detailedInfo = XStreamHandle.toBean(info, DetailedInfo.class);
        return detailedInfo;
    }
}
