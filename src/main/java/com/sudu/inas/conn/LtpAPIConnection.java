package com.sudu.inas.conn;

import com.sudu.inas.beans.TransElement;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class LtpAPIConnection {

    private final Logger log = Logger.getLogger(LtpAPIConnection.class);
    private final String prefix = "http://192.168.11.210:8020/ltp";


    private String text;

    private String x;

    private String t;

    public LtpAPIConnection(String text, String x, String t) {
        this.text = text;
        this.x = x;
        this.t = t;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPrefix() {
        return prefix;
    }

    public String toUrl() throws UnsupportedEncodingException {
        return prefix + "?s=" + URLEncoder.encode(text, "utf-8")
                + "&x=" + x
                + "&t=" + t;
    }

    //开启调用连接
    public TransElement openConnection() {
        Future<TransElement> future = ExecutorPool
                .singleTon().invoke(new LinkedThread(this));
        try {
            return future.get();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    class LinkedThread implements Callable<TransElement> {

        private LtpAPIConnection connection;

        public LinkedThread(LtpAPIConnection connection) {
            this.connection = connection;
        }

        @Override
        public TransElement call() throws Exception {
            try {
                HttpClient client = new HttpClient();
                System.out.println(connection.toUrl());
                GetMethod method = new GetMethod(connection.toUrl());
                int statuscode = client.executeMethod(method);
                if (statuscode == HttpStatus.SC_OK) {
                    byte[] m = method.getResponseBody();
                    log.info("open connection successful " + connection.getText());
                    TransElement trans = new TransElement(connection.getText(), connection.getT(), new String(m, "utf-8"));
                    log.info(Thread.currentThread().getName() + " get transElement invoke successful");
                    return trans;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                log.debug("正在关闭连接");
                LtpAPIConnectionFactory.closeConnection(connection);
            }
            return null;
        }

    }

}
