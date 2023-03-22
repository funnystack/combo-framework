package com.funny.combo.trace.http;

import com.funny.combo.trace.util.LogTraceUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * OkHttp 添加拦截器
 * private static OkHttpClient client = new OkHttpClient.Builder()
 *           .addNetworkInterceptor(new OkHttpTraceIdInterceptor())
 *           .build();
 */
public class OkHttpTraceIdInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String traceId = LogTraceUtil.getNowOrNewTraceId();;
        Request request = null;
        if (traceId != null) {
            //添加请求体
            request = chain.request().newBuilder().addHeader(LogTraceUtil.LOG_TRACE_ID, traceId).build();
        }
        Response originResponse = chain.proceed(request);
        return originResponse;
    }
}