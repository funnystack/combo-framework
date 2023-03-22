package com.funny.combo.trace.http;

import com.funny.combo.trace.util.LogTraceUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import java.util.List;


public class FeignTraceInterceptor implements RequestInterceptor{
    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            if (MDC.getMDCAdapter() != null) {
                boolean addTrace = true;
                List<String> vars =  requestTemplate.variables();
                if(vars != null &&  vars.size() > 0){
                    for(String param: vars){
                        if(param.equals(LogTraceUtil.LOG_TRACE_ID)){
                            addTrace = false;
                        }
                    }
                }
                if(addTrace){
                    String traceId = LogTraceUtil.getNowOrNewTraceId();
                    requestTemplate.header(LogTraceUtil.LOG_TRACE_ID,traceId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}