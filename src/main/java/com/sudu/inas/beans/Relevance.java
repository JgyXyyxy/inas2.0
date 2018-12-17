package com.sudu.inas.beans;

import com.google.common.base.MoreObjects;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "relevance",type = "simple")
public class Relevance {

    @Id
    private String rId;

    private String type;
    private String description;
    private String sourceEventId;
    private String sourceEntityId;
    private String targetEventId;
    private String targetEntityId;

    public Relevance() {
    }

    public Relevance(String rId) {
        this.rId = rId;
    }

    public Relevance(String rId, String type, String description, String sourceEventId, String sourceEntityId, String targetEventId, String targetEntityId) {
        this.rId = rId;
        this.type = type;
        this.description = description;
        this.sourceEventId = sourceEventId;
        this.sourceEntityId = sourceEntityId;
        this.targetEventId = targetEventId;
        this.targetEntityId = targetEntityId;
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

    public String getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(String sourceEntityId) {
        this.sourceEntityId = sourceEntityId;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("rId", rId)
                .add("type", type)
                .add("description", description)
                .add("sourceEventId", sourceEventId)
                .add("sourceEntityId", sourceEntityId)
                .add("targetEventId", targetEventId)
                .add("targetEntityId", targetEntityId)
                .toString();
    }
}
