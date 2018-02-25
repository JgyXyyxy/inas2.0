package com.sudu.inas.conn;

import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * LtpFactory模式
 *
 * @author ljh_2015
 */
public class LtpAPIConnectionFactory {

    private static final Logger log = Logger.getLogger(LtpAPIConnectionFactory.class);
    private static final int maxSize = 100;

    private static ArrayList<LtpAPIConnection> connections;
    private static final String api_key = "p8D9P0V1rsbV3aBKCudxIVwgpyjb8GjGFXKwHxsV";
    private static final String pattern = "all";
    private static final String format = "xml";

    static {
        log.debug("-------- ltp connections init -------");
        if (connections == null) {
            connections = new ArrayList<>();
        }
        for (int i = 0; i < maxSize; i++) {
            connections.add(new LtpAPIConnection(api_key, null, format, pattern));
        }
        log.debug("-------- ltp connections init successfully -------");
    }

    public static LtpAPIConnection configConnection(String text) {
        if (connections.isEmpty()) {
            return null;
        }
        int last = connections.size();
        LtpAPIConnection connection = connections.remove(last - 1);
        connection.setText(text);
        return connection;
    }

    public static void closeConnection(LtpAPIConnection api) {
        api.setText(null);
        connections.add(api);
    }

}
