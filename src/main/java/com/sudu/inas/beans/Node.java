package com.sudu.inas.beans;

import lombok.Data;

/**
 * Created by J on  17-10-27.
 */

@Data
public class Node {

    private int x;
    private int y;
    private String id;
    private String label;
    private int size;
    private String color;
    private int serial;

    public Node() {
    }

    public Node(int x, int y, String id, String label, int size, String color) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.label = label;
        this.size = size;
        this.color = color;
    }
}
