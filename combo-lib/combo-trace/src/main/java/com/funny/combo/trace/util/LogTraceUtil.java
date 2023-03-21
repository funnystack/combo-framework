package com.funny.combo.trace.util;

import org.slf4j.MDC;

/**
 * Created by funnystack on 20/3/10.
 */
public class LogTraceUtil {

    /**
     * 日志会话session，用于log4j配置的关键字
     * eg:
     * <param name="ConversionPattern" value="%d [%7r] %6p - %30.30c -%X{unique} %m \n"/>
     */
    public static final String LOG_SESSION_ID = "unique";

    /**
     * 日志跟踪Id
     */
    public static final String traceIdKey = "mallTraceId";

    public static final String EXT = "ext";

    /**
     * 生成新的日志跟踪Id
     * @return
     */
    public static String getNewTraceId() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 添加MDC日志跟踪Id
     */
    public static void doBefore() {
        try {
            if(MDC.getMDCAdapter() != null) {
                MDC.put(LogTraceUtil.LOG_SESSION_ID, LogTraceUtil.getNewTraceId());
            }
        }catch (Exception e){
        }
    }

    /**
     * 删除MDC日志跟踪Id
     */
    public static void doAfter() {
        try {
            if (MDC.getMDCAdapter() != null) {
                MDC.remove(LogTraceUtil.LOG_SESSION_ID);
            }
        }catch (Exception e) {
        }
    }
}
