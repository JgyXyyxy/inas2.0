package com.sudu.inas.beans;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RealEntity {

    private String objectId;
    private String realName;
    private String rawInfo;
    private Map<String, String> params = new HashMap<>();
    private ArrayList<Event> events = new ArrayList<>();

    public RealEntity(String objectId) {
    }

    public RealEntity() {
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


    public String getRawInfo() {
        return rawInfo;
    }

    public void setRawInfo(String rawInfo) {
        this.rawInfo = rawInfo;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
