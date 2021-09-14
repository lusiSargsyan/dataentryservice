package com.study.dataentryservice.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    Helper class to control logging format for centralized logging support.
*/
public class LogHelper {

    private static final Logger logger = LoggerFactory.getLogger(LogHelper.class);

    public static void logInfo(String ...message) {
        logger.info(String.join(": ", message));
    }

    public static void logError(Throwable e) {
        logger.error(e.getMessage());
    }

    public static void logError(String ...message) {
        logger.debug(String.join(": ", message));
    }

    public static void debug(String ...message) {
        logger.debug(String.join(": ", message));
    }
}
