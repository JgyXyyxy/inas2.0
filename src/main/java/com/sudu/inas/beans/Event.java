package com.sudu.inas.beans;

import com.google.common.base.MoreObjects;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "event",type = "simple")
public class Event {

    @Id
    private String eventId;

    private String objectId;
    private String ts;
    private String site;
    private String details;
    private String affect;

    public Event(String eventId, String objectId,String ts, String site, String details, String affect) {
        this.eventId = eventId;
        this.objectId = objectId;
        this.ts = ts;
        this.site = site;
        this.details = details;
        this.affect = affect;
    }

    public Event() {
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAffect() {
        return affect;
    }

    public void setAffect(String affect) {
        this.affect = affect;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("eventId", eventId)
                .add("ts", ts)
                .add("site", site)
                .add("details", details)
                .add("affect", affect)
                .toString();
    }

    public java.lang.String packEventLabel(){
        StringBuilder sb = new StringBuilder();
        sb.append("时间 "+ts+"  地点 "+site+"||");
        sb.append("详情 "+details+"||");
        return sb.toString();
    }
    public java.lang.String pack(){
        StringBuilder sb = new StringBuilder();
        sb.append(ts+" "+details);
        return sb.toString();
    }
}
