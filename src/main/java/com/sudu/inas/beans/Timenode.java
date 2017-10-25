package com.sudu.inas.beans;

import com.google.common.base.MoreObjects;
import lombok.Data;

/**
 * Created by J on  17-10-23.
 */

@Data
public class Timenode {

    private String timePoint;
    private DetailedInfo info;


    public Timenode() {
    }

    public Timenode(String timePoint) {
        this.timePoint = timePoint;
    }

    public Timenode(String timePoint, DetailedInfo info) {
        this.timePoint = timePoint;
        this.info = info;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("timePoint", timePoint)
                .add("info", info)
                .toString();
    }

    public static void main(String[] args) {
        DetailedInfo detailedInfo = new DetailedInfo("beijing","Olympic","success");
        Timenode timenode = new Timenode("2008-8-22", detailedInfo);
        System.out.println(timenode.toString());
    }
}
