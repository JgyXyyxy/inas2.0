package com.sudu.inas.service;

public interface RawinfoService {

    String findRealName(String objectId);
    String findRawText(String objectId);

    void addRealName(String realName,String objectId);

    void addRawText(String rawText,String objectId);
}
