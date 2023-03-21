package com.funny.combo.trace.log4j2;


import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kcq on 2017/6/9.
 */
public class Log4jMdcFilter extends OncePerRequestFilter {

    private String host;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
//        boolean isFirstRequest = !isAsyncDispatch(httpServletRequest);
//        if (isFirstRequest) {
//            Object traceId = httpServletRequest.getAttribute("trace_id");
//            if (traceId == null) {
//                httpServletRequest.setAttribute("trace_id", UUID.randomUUID().toString().replace("-", ""));
//            }
//        }
        HttpServletRequest requestToUse = httpServletRequest;
//        if (!(httpServletRequest instanceof ContentCachingRequestWrapper)) {
//            requestToUse = new ContentCachingRequestWrapper(httpServletRequest);
//        }
        Map<String, String> mdcMap = buildMdcMap(requestToUse);
        CloseableThreadContext.Instance ctc = putAll(mdcMap);
        try {
            filterChain.doFilter(requestToUse, httpServletResponse);
        } finally {
            if (ctc != null) {
                ctc.close();
            }
        }
    }

    private CloseableThreadContext.Instance putAll(Map<String, String> mdcMap) {
        CloseableThreadContext.Instance ctc = null;
        for (Map.Entry<String, String> entry : mdcMap.entrySet()) {
            if (ctc == null) {
                ctc = CloseableThreadContext.put(entry.getKey(), entry.getValue());
            }
            ctc.put(entry.getKey(), entry.getValue());
        }
        return ctc;
    }

    private Map<String, String> buildMdcMap(HttpServletRequest requestToUse) {
        Map<String, String> webAttributeMap = new HashMap();

        String queryString = requestToUse.getQueryString();
        webAttributeMap.put("queryString", queryString == null ? "-" : queryString);
        webAttributeMap.put("uri", requestToUse.getRequestURI());
        //add new mdc
        webAttributeMap.put("QueryString", queryString == null ? "-" : queryString);
        webAttributeMap.put("UriStem", requestToUse.getRequestURI().toLowerCase());
        webAttributeMap.put("Host", findHost(requestToUse));
//        webAttributeMap.put("ServerIP", requestToUse.getLocalAddr());
//        String formString = getRequestBody(requestToUse);
        String formString = null;
        webAttributeMap.put("FormString", formString == null ? "-" : formString);
        webAttributeMap.put("ContextPath", requestToUse.getContextPath().toLowerCase());
        webAttributeMap.put("TraceId", getTraceId(requestToUse));
        webAttributeMap.put("UserAgent", getHeader(requestToUse, "User-Agent"));

        return webAttributeMap;
    }

    private String getRequestBody(HttpServletRequest request) {
        if (StringUtils.isEmpty(request.getContentType())
                || !request.getContentType().contains("json")) {
            return null;
        }

        //如果不调用getParameter函数，request.getContentAsByteArray取不到数据
        if (request.getContentLength() == 0) {
            request.getParameter("");
        }

        return bodyContent(request);
    }

    private String bodyContent(HttpServletRequest request) {
        String payload = null;
        try {
            byte[] buffer = readStream(request.getInputStream());
            if (buffer.length > 0) {
                payload = new String(buffer, request.getCharacterEncoding());
                if (payload.length() > 2000) {
                    payload = payload.substring(0, 1999);
                }
            }
        } catch (Exception e) {
            payload = "[unknown]";
        }
        return payload;
    }

    private String getTraceId(HttpServletRequest request) {
        Object traceId = request.getAttribute("trace_id");
        return traceId == null ? "-" : (String) traceId;
    }

    public String findHost(HttpServletRequest requestToUse) {
        if (!StringUtils.isEmpty(host)) {
            return host;
        }
        return requestToUse.getServerName().toLowerCase();
    }


    private String getHeader(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        return value == null ? "-" : value;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
//        outStream.close();
//        inStream.close();
        return outStream.toByteArray();
    }
}
