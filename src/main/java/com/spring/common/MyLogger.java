package com.spring.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhanghua on 2018/2/27.
 */
public class MyLogger {
    /** 记录日志对象 */
    private Logger LOGGER;

    /**
     * 日志
     * */
    public MyLogger(Class<?> c) {
        LOGGER = LoggerFactory.getLogger(c);
    }

    /**
     * 日志记录
     *
     * @param message
     *            日志内容
     */
    public void error(String message) {
        LOGGER.error(message);
    }

    /**
     * 日志记录
     *
     * @param message
     */
    public void error(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }

    /**
     * 日志记录
     *
     * @param message
     */
    public void debug(String message) {
        LOGGER.debug(message);
    }

    /**
     * 记录信息
     *
     * @param message
     */
    public void info(Object message) {
        LOGGER.info(message.toString());
    }
}
