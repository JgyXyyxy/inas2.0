package com.sudu.inas.beans;


import com.sudu.inas.util.XStreamHandle;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.ArrayList;

@Data
@XStreamAlias("connectionlist")
public class ConnectionList {

    @XStreamImplicit
    private ArrayList<Connection> connections;

    public ConnectionList() {
    }

    public ConnectionList(ArrayList<Connection> connections) {
        this.connections = connections;
    }


}
