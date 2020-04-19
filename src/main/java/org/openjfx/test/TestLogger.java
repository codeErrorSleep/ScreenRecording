package org.openjfx.test;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* @Description:    测试日志框架
* @Author:         qiuShao
* @CreateDate:     20-4-18 上午11:17
*/
public class TestLogger {


    public static final Logger LOGGER = LoggerFactory.getLogger(TestLogger.class);
    public static void main(String[] args) {

        // ch.qos.logback.classic.Logger 可以设置日志的级别
        // 获取一个名为 "com.foo" 的 logger 实例
        ch.qos.logback.classic.Logger logger =
                (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("com.foo");
        // 设置 logger 的级别为 INFO
        logger.setLevel(Level.DEBUG);

        // 这条日志可以打印，因为 WARN >= INFO
        logger.warn("警告信息");
        // 这条日志不会打印，因为 DEBUG < INFO
        logger.debug("调试信息");

        // "com.foo.bar" 会继承 "com.foo" 的有效级别
        Logger barLogger = LoggerFactory.getLogger("com.foo.bar");
        // 这条日志会打印，因为 INFO >= INFO
        barLogger.info("子级信息");
        // 这条日志不会打印，因为 DEBUG < INFO
        barLogger.debug("子级调试信息");

        logger.info("logback 成功了");
        logger.error("logback 成功了");
        logger.debug("logback 成功了");

    }
}