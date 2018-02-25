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
    private final String prefix = "http://api.ltp-cloud.com/analysis/?";

    private String api_key;

    private String text;

    private String format;

    private String pattern;

    public LtpAPIConnection(String api_key, String text, String format,
                            String pattern) {
        this.api_key = api_key;
        this.text = text;
        this.format = format;
        this.pattern = pattern;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPrefix() {
        return prefix;
    }

    public String toUrl() throws UnsupportedEncodingException {
        return prefix + "api_key=" + api_key
                + "&text=" + URLEncoder.encode(text, "utf-8")
                + "&format=" + format
                + "&pattern=" + pattern;
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
                GetMethod method = new GetMethod(connection.toUrl());
                int statuscode = client.executeMethod(method);
                if (statuscode == HttpStatus.SC_OK) {
                    byte[] m = method.getResponseBody();
                    log.info("open connection successful " + connection.getText());
                    TransElement trans = new TransElement(connection.getText(), connection.getFormat(), new String(m, "utf-8"));
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
