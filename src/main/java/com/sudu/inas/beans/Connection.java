package com.sudu.inas.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;


/**
 * Created by J on  17-10-30.
 */

@Data
@XStreamAlias("connection")
public class Connection {

    @XStreamAlias("No")
    private int connNo;
    @XStreamAlias("connId")
    private String connObjectId;
    @XStreamAlias("connTime")
    private String conntimePoint;
    @XStreamAlias("influence")
    private String influence;

    public Connection() {
    }

    public Connection(String connObjectId, String conntimePoint, String influence) {
        this.connObjectId = connObjectId;
        this.conntimePoint = conntimePoint;
        this.influence = influence;
    }
}
