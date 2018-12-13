package com.sudu.inas.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "relevance",type = "simple")
public class Relevance {

    @Id
    private String rId;

    private String type;
    private String description;
    private String sourceEventId;
    private String targetEventId;

    public Relevance(String rId) {
        this.rId = rId;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceEventId() {
        return sourceEventId;
    }

    public void setSourceEventId(String sourceEventId) {
        this.sourceEventId = sourceEventId;
    }

    public String getTargetEventId() {
        return targetEventId;
    }

    public void setTargetEventId(String targetEventId) {
        this.targetEventId = targetEventId;
    }

    public String getParam(String input){
        switch (input){
            case "rId": return getrId();
            case "type":return getType();
            case "description":return getDescription();
            case "sourceEventId":return getSourceEventId();
            case "targetEventId":return getTargetEventId();
            default:return "error in get";
        }
    }
}
