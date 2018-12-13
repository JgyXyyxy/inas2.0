package com.sudu.inas.conn;

import com.sudu.inas.beans.TransElement;
import org.apache.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ResultCache extends LinkedBlockingQueue<TransElement> {


    private static final long serialVersionUID = 1L;
    private final static Logger log = Logger.getLogger(ResultCache.class);
    private final static ResultCache _newInstance = new ResultCache();
    public static ResultCache newInstance() {
        return _newInstance;
    }


    public void putElement(TransElement e, String... params) {
        try {
            if (e!=null){
                this.put(e);
                log.debug("receive element");
            }else {
                log.debug("element is null");
            }
        } catch (InterruptedException e1) {
            log.error(e1.getMessage());
        }
    }


    public TransElement pollElement() {
        try {
            return this.poll(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            return null;
        } finally {
            log.debug("get element");
        }
    }
}
