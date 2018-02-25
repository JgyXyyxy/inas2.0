package com.sudu.inas.conn;


import com.sudu.inas.beans.TransElement;
import org.apache.log4j.Logger;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * 线程池
 *
 * @author ljh_2015
 */
public class ExecutorPool {

    private final Logger log = Logger.getLogger(ExecutorPool.class);
    private final int numberOfCores = Runtime.getRuntime().availableProcessors();
    private final double blockingCoefficient = 0.8;
    private final int poolSize = (int) (numberOfCores / (1 - blockingCoefficient));
    private final ExecutorService executorPool = Executors.newFixedThreadPool(poolSize);
    private static final ExecutorPool singleton = new ExecutorPool();

    public static ExecutorPool singleTon() {
        return singleton;
    }

    public final Future<TransElement> invoke(Callable<TransElement> call) {
        return executorPool.submit(call);
    }

}
