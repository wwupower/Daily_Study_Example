

package com.power.validator.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.LoggerFactory;


public class LoggerUtil {

    /**
     * 输出debug级别的日志
     * 
     * @param clazz
     * @param msg
     */
    public static void debug(Class clazz, String msg) {
        LoggerFactory.getLogger(clazz).debug(resetMessage(msg));
    }

    /**
     * 输出debug级别的日志
     * 
     * @param clazz
     * @param format
     * @param arguments
     */
    public static void debug(Class clazz, String format, Object... arguments) {
        LoggerFactory.getLogger(clazz).debug(resetMessage(format), arguments);
    }

    /**
     * 输出debug级别的日志
     * 
     * @param clazz
     * @param msg
     * @param t
     */
    public static void debug(Class clazz, String msg, Throwable t) {
        LoggerFactory.getLogger(clazz).debug(resetMessage(msg), t);
    }

    /**
     * 输出info级别的日志
     * 
     * @param clazz
     * @param msg
     */
    public static void info(Class clazz, String msg) {
        LoggerFactory.getLogger(clazz).info(resetMessage(msg));
    }

    /**
     * 输出info级别的日志
     * 
     * @param clazz
     * @param format
     * @param arguments
     */
    public static void info(Class clazz, String format, Object... arguments) {
        LoggerFactory.getLogger(clazz).info(resetMessage(format), arguments);
    }

    /**
     * 输出info级别的日志
     * 
     * @param clazz
     * @param msg
     * @param t
     */
    public static void info(Class clazz, String msg, Throwable t) {
        LoggerFactory.getLogger(clazz).info(resetMessage(msg), t);
    }

    /**
     * 输出warn级别的日志
     * 
     * @param clazz
     * @param msg
     */
    public static void warn(Class clazz, String msg) {
        LoggerFactory.getLogger(clazz).warn(resetMessage(msg));
    }

    /**
     * 输出warn级别的日志
     * 
     * @param clazz
     * @param format
     * @param arguments
     */
    public static void warn(Class clazz, String format, Object... arguments) {
        LoggerFactory.getLogger(clazz).warn(resetMessage(format), arguments);
    }

    /**
     * 输出warn级别的日志
     * 
     * @param clazz
     * @param msg
     * @param t
     */
    public static void warn(Class clazz, String msg, Throwable t) {
        LoggerFactory.getLogger(clazz).warn(resetMessage(msg), t);
    }

    /**
     * 输出error级别的日志
     * 
     * @param clazz
     * @param msg
     */
    public static void error(Class clazz, String msg) {
        LoggerFactory.getLogger(clazz).error(resetMessage(msg));
    }

    /**
     * 输出error级别的日志
     * 
     * @param clazz
     * @param format
     * @param arguments
     */
    public static void error(Class clazz, String format, Object... arguments) {
        LoggerFactory.getLogger(clazz).error(resetMessage(format), arguments);
    }

    /**
     * 输出error级别的日志
     * 
     * @param clazz
     * @param msg
     * @param t
     */
    public static void error(Class clazz, String msg, Throwable t) {
        LoggerFactory.getLogger(clazz).error(resetMessage(msg), t);
    }

    /**
     * 输出alert级别的日志
     * 
     * @param clazz
     * @param msg
     */
    public static void alert(Class clazz, String msg) {
        LoggerFactory.getLogger("ALERT").error(resetMessage(msg));
    }

    /**
     * 输出alert级别的日志
     * 
     * @param clazz
     * @param format
     * @param arguments
     */
    public static void alert(Class clazz, String format, Object... arguments) {
        String msg = resetMessage(format);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = fmt.format(new Date());
        StringBuilder sbLog = new StringBuilder(100);
        sbLog.append(time).append(" [ ").append(clazz.getName()).append(" ] - [").append(" ALERT ] ").append(msg);
        LoggerFactory.getLogger("ALERT").error(sbLog.toString(), arguments);
    }

    /**
     * 输出alert级别的日志
     * 
     * @param clazz
     * @param msg
     * @param t
     */
    public static void alert(Class clazz, String msg, Throwable t) {
        LoggerFactory.getLogger(clazz).error(resetMessage(msg), t);
    }

    /**
     * 为日志内容添加关键内容
     *
     * @param msg 日志内容
     * @return
     */
    private static String resetMessage(String msg) {
        if (null == msg) {
            msg = "Unknow error。";
        }
        StringBuilder sbMsg = new StringBuilder(msg.length() + 150);
        return sbMsg.toString();
    }

}
