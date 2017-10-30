package com.sudu.inas.beans;

import lombok.Data;

/**
 * Created by J on  17-10-27.
 */

@Data
public class Edge {

    private String sourceID;
    private String targetID;

    public Edge() {
    }

    public Edge(String sourceID, String targetID) {
        this.sourceID = sourceID;
        this.targetID = targetID;
    }
}
