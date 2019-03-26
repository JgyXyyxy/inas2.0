package com.sudu.inas.beans;

import lombok.Data;

@Data
public class Tuple {

    private String key;
    private String value;

    public Tuple(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
