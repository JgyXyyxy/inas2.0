package com.sudu.inas.service.impl;

import com.sudu.inas.beans.*;
import com.sudu.inas.repository.EventRepository;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.service.ObjectService;
import com.sudu.inas.util.HbaseModelUtil;
import com.sudu.inas.util.XStreamHandle;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by J on  17-10-27.
 */

@Service("ObjectService")
public class ObjectServiceImpl implements ObjectService {

    @Autowired
    HbaseDao hbaseDao;

    @Autowired
    EventRepository eventRepository;


    @Override
    public Entity findObjectById(String objectId) {
        Result result = hbaseDao.getDataFromRowkey(HbaseModelUtil.BASICTABLE, objectId);
        Entity entity = new Entity();
        entity.setObjectId(objectId);
        for (KeyValue kv : result.list()) {
            HbaseModel hbaseModel = HbaseModelUtil.kvToHbaseModel(kv);
            Entity entity1 = insertSingleModel(entity, hbaseModel);
            entity = entity1;
        }
        return entity;
    }

    @Override
    public RealEntity findEntityById(String objectId) throws Exception {
        Result result = hbaseDao.getDataFromRowkey(HbaseModelUtil.BASICTABLE, objectId);
        RealEntity realEntity = new RealEntity();
        realEntity.setObjectId(objectId);
        for (KeyValue kv : result.list()) {
            HbaseModel hbaseModel = HbaseModelUtil.kvToHbaseModel(kv);
            realEntity = packageModel(realEntity, hbaseModel);
        }
        return realEntity;
    }

    @Override
    public RealEntity findEntityByIdFromEs(String objectId) throws Exception {
        Result result = hbaseDao.getDataFromRowkey(HbaseModelUtil.BASICTABLE, objectId);
        RealEntity realEntity = new RealEntity();
        realEntity.setObjectId(objectId);
        for (KeyValue kv : result.list()) {
            HbaseModel hbaseModel = HbaseModelUtil.kvToHbaseModel(kv);
            realEntity = packModelFromEs(realEntity, hbaseModel);
        }
        return realEntity;
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
        while (iterator1.hasNext()) {
            String s = iterator1.next();
            Entity entity = entityHashMap.get(s);
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public List<RealEntity> findEntitiesByPrefix(String prefix) throws Exception {
        List<Result> rets = hbaseDao.getDataWithSameBegining(HbaseModelUtil.BASIC_TABLE, prefix);
        Iterator<Result> iterator = rets.iterator();
        HashMap<String, RealEntity> entityHashMap = new HashMap<>();
        while (iterator.hasNext()) {
            Result ret = iterator.next();
            for (KeyValue kv : ret.list()) {
                HbaseModel hbaseModel = HbaseModelUtil.kvToHbaseModel(kv);
                if (entityHashMap.containsKey(hbaseModel.getRow())) {
                    RealEntity entity = entityHashMap.get(hbaseModel.getRow());
                    RealEntity realEntity = packageModel(entity, hbaseModel);
                    entityHashMap.remove(hbaseModel.getRow());
                    entityHashMap.put(hbaseModel.getRow(), realEntity);

                } else {
                    RealEntity entity = new RealEntity();
                    entity.setObjectId(hbaseModel.getRow());
                    RealEntity realEntity = packageModel(entity, hbaseModel);
                    entityHashMap.put(hbaseModel.getRow(), realEntity);
                }
            }
        }

        ArrayList<RealEntity> entities = new ArrayList<>();
        Iterator<String> iterator1 = entityHashMap.keySet().iterator();
        while (iterator1.hasNext()) {
            String s = iterator1.next();
            RealEntity entity = entityHashMap.get(s);
            entities.add(entity);
        }
        return entities;
    }


    public Entity insertSingleModel(Entity entity, HbaseModel hbaseModel) {
        String eventTs = hbaseModel.getQualifier();
        hbaseDao.getDataFromRowkey(HbaseModelUtil.EVENTS_TABLE, eventTs);
        ArrayList<Timenode> timeLine = entity.getTimeLine();
        if (HbaseModelUtil.BASIC_EVENT.equals(hbaseModel.getFamilyName())) {
            Timenode timenode = new Timenode();
            timenode.setTimePoint(hbaseModel.getQualifier());
            timenode.setInfo(XStreamHandle.toBean(hbaseModel.getValue(), DetailedInfo.class));
            timeLine.add(timenode);
        } else {
            if (HbaseModelUtil.COLUMN2.equals(hbaseModel.getQualifier())) {
                entity.setRawInfo(hbaseModel.getValue());
            } else {
                entity.setRealName(hbaseModel.getValue());
            }
        }
        return entity;
    }

    public RealEntity packageModel(RealEntity realEntity, HbaseModel hbaseModel) {
        System.out.println(hbaseModel.getFamilyName());
        ArrayList<Event> eventList = realEntity.getEvents();
        if (HbaseModelUtil.BASIC_EVENT.equals(hbaseModel.getFamilyName())) {
            Event event = new Event();
            String eventId = hbaseModel.getValue();
            Result ret = hbaseDao.getDataFromRowkey(HbaseModelUtil.EVENTS_TABLE, eventId);
            for (KeyValue kv : ret.list()) {
                HbaseModel model = HbaseModelUtil.kvToHbaseModel(kv);
                String value = model.getValue();
                switch (model.getQualifier()) {
                    case "ts":
                        event.setTs(value);
                        break;
                    case "site":
                        event.setSite(value);
                        break;
                    case "details":
                        event.setDetails(value);
                        break;
                    default:
                        event.setAffect(value);
                }
                event.setEventId(eventId);
            }
            eventList.add(event);
        }
        if (HbaseModelUtil.BASIC_RAW.equals(hbaseModel.getFamilyName())) {
            if (hbaseModel.getQualifier().equals(HbaseModelUtil.COLUMN2)) {
                realEntity.setRawInfo(hbaseModel.getValue());
            } else if (hbaseModel.getQualifier().equals(HbaseModelUtil.COLUMN1)) {
                realEntity.setRealName(hbaseModel.getValue());
            } else {
                Map<String, String> params = realEntity.getParams();
                params.put(hbaseModel.getQualifier(), hbaseModel.getValue());
                realEntity.setParams(params);
            }

        }
        return realEntity;
    }

    public RealEntity packModelFromEs(RealEntity realEntity, HbaseModel hbaseModel) {
        System.out.println(hbaseModel.getFamilyName());
        ArrayList<Event> eventList = realEntity.getEvents();
        if (HbaseModelUtil.BASIC_EVENT.equals(hbaseModel.getFamilyName())) {
            Event event = new Event();
            String eventId = hbaseModel.getValue();
            Event eventByEventId = eventRepository.queryEventByEventId(eventId);
            if (eventByEventId!=null){
                eventList.add(eventByEventId);
            }

        }
        if (HbaseModelUtil.BASIC_RAW.equals(hbaseModel.getFamilyName())) {
            if (hbaseModel.getQualifier().equals(HbaseModelUtil.COLUMN2)) {
                realEntity.setRawInfo(hbaseModel.getValue());
            } else if (hbaseModel.getQualifier().equals(HbaseModelUtil.COLUMN1)) {
                realEntity.setRealName(hbaseModel.getValue());
            } else {
                Map<String, String> params = realEntity.getParams();
                params.put(hbaseModel.getQualifier(), hbaseModel.getValue());
                realEntity.setParams(params);
            }

        }
        realEntity.setEvents(eventList);
        return realEntity;
    }


}
