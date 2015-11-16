package com.sproutsocial.nsq;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;

class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static ThreadFactory threadFactory(String name) {
        return new ThreadFactoryBuilder()
                .setNameFormat(name + "-%d")
                .setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        logger.error("uncaught error", e); //doesn't catch everything, just for extra safety
                    }
                })
                .build();
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        }
        catch (IOException e) {
            logger.warn("problem closing. {}", e.toString());
        }
    }

    public static void sleepQuietly(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void cancel(ScheduledFuture task) {
        if (task != null) {
            task.cancel(false);
        }
    }

    //useful for jmx
    public static List<String> sortedStrings(Collection col) {
        List<String> res = new ArrayList<String>(col.size());
        for (Object obj : col) {
            res.add(obj.toString());
        }
        Collections.sort(res);
        return res;
    }

}
