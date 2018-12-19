package com.sudu.inas.service.impl;

import com.sudu.inas.beans.HbaseModel;
import com.sudu.inas.beans.Relevance;
import com.sudu.inas.repository.HbaseDao;
import com.sudu.inas.repository.RelevanceRepository;
import com.sudu.inas.service.RelevanceService;
import com.sudu.inas.util.HbaseModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Service("RelevanceService")
public class RelevanceServiceImpl implements RelevanceService {

    @Autowired
    RelevanceRepository relevanceRepository;

    @Autowired
    HbaseDao hbaseDao;

    @Override
    public Relevance getRelevanceByReId(String reId) {
        return relevanceRepository.queryRelevanceByRId(reId);
    }

    @Override
    public Relevance addRelevance(Relevance relevance) {

        Field[] declaredFields = relevance.getClass().getDeclaredFields();
        for (int i =0;i<declaredFields.length;i++){
            String field = declaredFields[i].getName();
            hbaseDao.insertData(HbaseModelUtil.RELEVANCES_TABLE,relevance.getrId(),HbaseModelUtil.RELEVANCES_PARAMS,field,relevance.getParam(field),null);
        }
        return relevanceRepository.save(relevance);
    }

    @Override
    public void delRelevance(String rId) {
        relevanceRepository.delete(rId);
    }

    @Override
    public List<Relevance> getRelevancesByEventId(String EventId) {
        List<Relevance> outRelevances = relevanceRepository.queryEventsBySourceEventId(EventId);
        List<Relevance> inRelevances = relevanceRepository.queryRelevancesByTargetEventId(EventId);
        ArrayList<Relevance> relevances = new ArrayList<>();
        if (inRelevances.size()!=0){
            relevances.addAll(inRelevances);
        }
        if (outRelevances.size()!=0){
            relevances.addAll(outRelevances);
        }
        return relevances;

    }

    @Override
    public List<Relevance> getRelevancesByEntityIds(String sourceId, String targetId) {
        return relevanceRepository.queryRelevancesBySourceEntityIdAndTargetEntityId(sourceId, targetId);
    }

    @Override
    public List<Relevance> getActiveRelevancesForEntity(String objectId) {
        return relevanceRepository.queryRelevancesBySourceEntityId(objectId);
    }

    @Override
    public List<Relevance> getPassiveRelevancesForEntity(String objectId) {
        return relevanceRepository.queryRelevancesByTargetEntityId(objectId);
    }

    public static void main(String[] args) {
        Relevance ddddd = new Relevance("ddddd");
        Field[] declaredFields = ddddd.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            System.out.println(declaredFields[i].getName());
        }
    }


}
