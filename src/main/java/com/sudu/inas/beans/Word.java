package com.sudu.inas.beans;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Word {

    private int id;
    private String cont;
    private String pos;
    private  String ne;
    private Integer parent;
    private String relate;
    private Integer semparent;
    private String semrelate;
    private List<Arg> args = new ArrayList<>();

    public Word() {
    }

}
