package com.sudu.inas.beans;

import com.google.common.base.MoreObjects;
import lombok.Data;

@Data
public class Action {

    private String entityName;
    private String timepoint;
    private String location;
    private String description;

    public Action(String entityName, String timepoint, String location, String description) {
        this.entityName = entityName;
        this.timepoint = timepoint;
        this.location = location;
        this.description = description;
    }

    public Action() {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("entityName", entityName)
                .add("timepoint", timepoint)
                .add("location", location)
                .add("description", description)
                .toString();
    }
}
