package com.funny.combo.trace.http;

import com.funny.combo.trace.util.LogTraceUtil;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * 为 RestTemplate 添加拦截器
 * restTemplate.setInterceptors(Arrays.asList(new RestTemplateTraceIdInterceptor()));
 */
public class RestTemplateTraceIdInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String traceId = LogTraceUtil.getNowOrNewTraceId();;
        if (traceId != null) {
            httpRequest.getHeaders().add(LogTraceUtil.LOG_TRACE_ID, traceId);
        }
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}