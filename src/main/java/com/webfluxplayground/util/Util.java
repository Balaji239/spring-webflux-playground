package com.webfluxplayground.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Util {

    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static void threadSleep(Duration duration){
        try{
            Thread.sleep(duration);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public static void threadSleep(long milliseconds){
        try{
            Thread.sleep(milliseconds);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }

}
