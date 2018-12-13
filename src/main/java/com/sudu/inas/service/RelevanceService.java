package com.sudu.inas.service;

import com.sudu.inas.beans.Relevance;

import java.util.List;
import java.util.Map;

public interface RelevanceService {

        Relevance getRelevanceByReId(String reId);
        Relevance addRelevance(Relevance relevance);
        int delRelevance(String rId);
        List<Relevance> getRelevancesByEventId(String EventId);
}
