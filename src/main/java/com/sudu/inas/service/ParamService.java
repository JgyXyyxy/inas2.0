package com.sudu.inas.service;

import com.sudu.inas.beans.Tuple;

import java.util.List;

public interface ParamService {

    void delParam(String objectId,String key);
    void insertParam(String objectId,String key,String value);
    List<Tuple> getAllParams(String objectId);
}
