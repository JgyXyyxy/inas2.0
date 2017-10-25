package com.sudu.inas.beans;

import lombok.Data;

/**
 * Created by J on 2017/8/15.
 */

@Data
public class HbaseModel {

    private String row;

    private  String familyName;

    private  String qualifier;

    private String value;

    public HbaseModel() {
    }

    public HbaseModel(String row, String familyName, String qualifier, String value) {
        this.row = row;
        this.familyName = familyName;
        this.qualifier = qualifier;
        this.value = value;
    }
}
