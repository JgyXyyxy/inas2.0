package com.sudu.inas.beans;

import com.google.common.base.MoreObjects;
import com.sudu.inas.util.CommonUtil;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by J on  17-10-23.
 */

@Data
public class Entity {

    private String objectId;
    private String realName;
    private String rawInfo;
    private ArrayList<Timenode> timeLine;

    public Entity() {
        timeLine = new ArrayList<>();
    }

    public Entity(String realName) {
        this.realName = realName;
        timeLine = new ArrayList<>();
    }

    public Entity(String objectId, String realName, String rawInfo, ArrayList<Timenode> timeLine) {
        this.objectId = objectId;
        this.realName = realName;
        this.rawInfo = rawInfo;
        this.timeLine = timeLine;
    }

    public void setRealName(String realName) {
        this.realName = realName;

    }

//    public void init(){
//        objectId = realName+ CommonUtil.genRandomNum();
//    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("objectId", objectId)
                .add("realName", realName)
                .add("rawInfo", rawInfo)
                .add("timeLine", timeLine)
                .toString();
    }

    public static void main(String[] args) {
        Entity entity1 = new Entity("libai");
        System.out.println(entity1.getObjectId());
        Entity entity2 = new Entity();
        entity2.setRealName("dufu");
        System.out.println(entity2.getObjectId());
    }
}
