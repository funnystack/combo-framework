package com.funny.combo.trace.http;

import com.funny.combo.trace.util.LogTraceUtil;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * 为HttpClient 添加拦截器
 * private static CloseableHttpClient httpClient = HttpClientBuilder.create()
 *             .addInterceptorFirst(new HttpClientTraceIdInterceptor())
 *             .build();
 */
public class HttpClientTraceIdInterceptor implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        String traceId = LogTraceUtil.getNowOrNewTraceId();
        //当前线程调用中有traceId，则将该traceId进行透传
        if (traceId != null) {
            //添加请求体
            httpRequest.addHeader(LogTraceUtil.LOG_TRACE_ID, traceId);
        }
    }
}