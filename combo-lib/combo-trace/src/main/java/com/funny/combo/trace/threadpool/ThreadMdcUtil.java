package com.funny.combo.trace.threadpool;

import com.funny.combo.trace.util.LogTraceUtil;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.funny.combo.trace.util.LogTraceUtil.LOG_SESSION_ID;

public class ThreadMdcUtil {
    public static void setTraceIdIfAbsent() {
        if (MDC.get(LOG_SESSION_ID) == null) {
            MDC.put(LOG_SESSION_ID, LogTraceUtil.getNewTraceId());
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}