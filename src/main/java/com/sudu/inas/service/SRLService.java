package com.sudu.inas.service;

import com.sudu.inas.beans.Action;
import com.sudu.inas.beans.SyntaxResult;
import com.sudu.inas.beans.TransElement;
import com.sudu.inas.conn.LtpAPIConnectionFactory;
import com.sudu.inas.conn.ResultCache;
import com.sudu.inas.util.ParseXml;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class SRLService {

    private final Logger log = Logger.getLogger(SRLService.class);
    private final ResultCache resltCache = ResultCache.newInstance();
    private final BlockingQueue<String> requestCache = new LinkedBlockingQueue<>();


    public void putSentence(String text) throws InterruptedException {
        log.info("put request: " + text);
        requestCache.put(text);
    }

    public void parseSingleSentence(String text) {
        TransElement element = LtpAPIConnectionFactory.configConnection(text).openConnection();
        resltCache.putElement(element);
    }

    public List<Action> start() throws InterruptedException {
        ArrayList<Action> actions = new ArrayList<>();
        while (!requestCache.isEmpty()){
            try {
                String requestText = requestCache.poll(2, TimeUnit.SECONDS);
                if (requestText != null) {
                    log.debug("get request: " + requestText);
                    parseSingleSentence(requestText);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        while (!resltCache.isEmpty()) {
            TransElement element = resltCache.pollElement();
            if (element != null) {
                try {
                    SyntaxResult syntaxResult = ParseXml.parseSyntax(element.getBody());
                    try {
                        List<Action> singleTrans = syntaxResult.dotrans();
                        actions.addAll(singleTrans);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return actions;

    }

}
