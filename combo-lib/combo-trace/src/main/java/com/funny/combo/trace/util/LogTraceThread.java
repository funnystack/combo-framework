package com.funny.combo.trace.util;

import com.funny.combo.trace.util.LogTraceUtil;
import org.slf4j.MDC;

/**
 * 线程日志traceId跟踪 {@link java.lang.Runnable}
 *
 * @author funnystack
 */
public abstract class LogTraceThread  implements Runnable {

    private String traceId;

    public LogTraceThread() {
        if (MDC.getMDCAdapter() != null) {
            traceId = MDC.get(LogTraceUtil.LOG_SESSION_ID);
            if (traceId == null || traceId.length() == 0) {
                traceId = LogTraceUtil.getNewTraceId();
            }
        }
        System.out.println(Thread.currentThread().getName()+" LogTraceThread:"+traceId);
    }

    public abstract void runTask();

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName()+" run:"+traceId);

        MDC.put(LogTraceUtil.LOG_SESSION_ID, traceId);

        try {
            // 执行任务
            runTask();
        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally {
            if (MDC.getMDCAdapter() != null) {
                MDC.remove(LogTraceUtil.LOG_SESSION_ID);
            }
        }
    }

    public void start(){
        new Thread(this).start();
    }
}
