package com.funny.combo.trace.util;

import org.slf4j.MDC;

import java.util.Random;

/**
 * Created by funnystack on 20/3/10.
 */
public class LogTraceUtil {

    /**
     * 日志跟踪Id，用于log4j配置的关键字
     * eg:
     * <param name="ConversionPattern" value="%d [%7r] %6p - %30.30c -%X{traceId} %m \n"/>
     */
    public static final String LOG_TRACE_ID = "traceId";
    private static final String RANDOM_STRING = "0123456789";
    private static Random random = new Random();
    /**
     * 生成新的日志跟踪Id
     * 32位自定义traceId：167953664816795367104327993efd18
     * 1679536648  1679536710   4327993efd18
     *    |         |             |
     * 高10位(IP) 中10位(Timestmap) 低12位(Random)
     * 通过 TraceID 反向解析时间戳，锁定时间范围，有助于提高存储库 Clickhouse 的检索效率，此外也能帮助决定当前的 Trace 应该查询热库还是冷库。
     * 绑定实例 IP，有助于关联当前 Trace 流量入口所属的实例，在某些极端场景，当链路上的节点检索不到时，也能通过实例和时间两个要素来做溯源。
     * @return
     */
    public static String getNewTraceId() {
        return LocalHostUtils.getIpFromString(LocalHostUtils.getLocalIp()) + System.currentTimeMillis() + getRandomString(12);
    }

    private static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(10);
            sb.append(RANDOM_STRING.charAt(number));
        }
        return sb.toString();
    }

    public static String getNowOrNewTraceId() {
        return MDC.getMDCAdapter() == null || MDC.get(LogTraceUtil.LOG_TRACE_ID) == null ?
                LogTraceUtil.getNewTraceId() : MDC.get(LogTraceUtil.LOG_TRACE_ID);
    }



    /**
     * 添加MDC日志跟踪Id
     */
    public static void doBefore() {
        try {
            if(MDC.getMDCAdapter() != null) {
                MDC.put(LogTraceUtil.LOG_TRACE_ID, LogTraceUtil.getNewTraceId());
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
                MDC.remove(LogTraceUtil.LOG_TRACE_ID);
            }
        }catch (Exception e) {
        }
    }
}
