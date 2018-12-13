package com.sudu.inas.repository;

import com.sudu.inas.beans.Relevance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RelevanceRepository extends ElasticsearchRepository<Relevance,String> {

    Relevance queryRelevanceByRId(String rId);
    List<Relevance> queryEventsBySourceEventId(String source);
    List<Relevance> queryRelevancesByTargetEventId(String target);
    void deleteRelevanceByTargetEventId(String target);
    void deleteRelevanceBySourceEventId(String source);
}
