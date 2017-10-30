package com.sudu.inas.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
@XStreamAlias("connection")
public class Connection {

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
