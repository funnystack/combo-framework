package com.funny.combo.trace.http;

import com.funny.combo.trace.util.LogTraceUtil;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志跟踪拦截器
 * Created by funnystack on 17/3/10.
 */
public class LogTraceInterceptor extends HandlerInterceptorAdapter {

    /**
     * 请求被处理前执行
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {

        try {
            if (MDC.getMDCAdapter() != null) {
                clearMDC();
                String traceId = request.getHeader(LogTraceUtil.LOG_TRACE_ID);
                if (traceId == null || traceId.length() == 0) {
                    traceId = LogTraceUtil.getNewTraceId();
                }
                MDC.put(LogTraceUtil.LOG_TRACE_ID, traceId);
            }
        } catch (Exception e) {
        }

        return true;
    }

    /**
     * 请求被完全处理后执行
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        try {
            if (MDC.getMDCAdapter() != null) {
                clearMDC();
            }
        } catch (Exception e) {
        }
    }


    private void clearMDC() {
        MDC.remove(LogTraceUtil.LOG_TRACE_ID);
    }

}

