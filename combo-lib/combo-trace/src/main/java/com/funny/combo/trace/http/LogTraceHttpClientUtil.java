package com.funny.combo.trace.http;

import com.funny.combo.trace.util.LogTraceUtil;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 带有日志跟踪的httpClient
 * Created by funnystack on 17/3/10.
 */
public class LogTraceHttpClientUtil {

//    private static final Logger logger = Logger.getLogger(LogTraceHttpClientUtil.class);

    /**
     * 发送HttpPost请求
     * @param url
     * @param param
     * @param timeout
     * @return
     */
    public static String sendPost(String url, Map<String, String> param, int timeout) {

//        logger.info("send http post:" + url + ",param:" + (param == null ? "" : param.toString()));

        if (url == null || url.length() == 0) {
            return null;
        }

        if (timeout <= 0) {
            timeout = 6000;
        }

        String traceId = MDC.getMDCAdapter() == null || MDC.get(LogTraceUtil.LOG_SESSION_ID) == null ?
                LogTraceUtil.getNewTraceId() : MDC.get(LogTraceUtil.LOG_SESSION_ID).toString();

        StringBuilder sb = new StringBuilder("&").append(LogTraceUtil.traceIdKey).append("=").append(traceId);
        if (url.contains("?")) {
            if (url.endsWith("&") || url.endsWith("?")) {
                url += sb.substring(1);
            } else {
                url += sb.toString();
            }
        } else {
            url += "?" + sb.substring(1);
        }


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).build();
        httpPost.setConfig(requestConfig);

        if (param != null && param.size() > 0) {

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            Iterator<Map.Entry<String, String>> it = param.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

        }

        CloseableHttpResponse response = null;
        String body = null;

        try {
            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                body = EntityUtils.toString(response.getEntity());
//                logger.info("http post response:" + body);
                return body;
            } else {
//                logger.info("http post response statusCode:" + response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
//            logger.error("error when send post request from: " + url);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
//                logger.error("error to close httpClient source.");
            }
            try {
                httpClient.close();
            } catch (Exception e) {
//                logger.error("error to close httpClient source.");
            }
        }

        return body;

    }

    /**
     * 发送HttpGet请求
     * @param url
     * @param param
     * @param timeout
     * @return
     */
    public static String sendGet(String url, Map<String, String> param, int timeout) {

//        logger.info("send http get:" + url + ",param:" + (param == null ? "" : param.toString()));

        if (url == null || url.length() == 0) {
            return null;
        }

        if (timeout <= 0) {
            timeout = 6000;
        }

        String traceId = MDC.getMDCAdapter() == null || MDC.get(LogTraceUtil.LOG_SESSION_ID) == null ?
                LogTraceUtil.getNewTraceId() : MDC.get(LogTraceUtil.LOG_SESSION_ID).toString();

        StringBuilder sb = new StringBuilder("&").append(LogTraceUtil.traceIdKey).append("=").append(traceId);

        if (param != null && param.size() >= 0) {
            for (String key : param.keySet()) {
                sb.append("&").append(key).append("=").append(param.get(key) == null ? "" : param.get(key));
            }
        }

        if (url.contains("?")) {
            if (url.endsWith("&") || url.endsWith("?")) {
                url += sb.substring(1);
            } else {
                url += sb.toString();
            }
        } else {
            url += "?" + sb.substring(1);
        }

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).build();
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        String body = null;

        try {
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                body = EntityUtils.toString(response.getEntity());
//                logger.info("http get response:" + body);
                return body;
            } else {
//                logger.info("http get response statusCode:" + response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
//            logger.error("error when send post request from: " + url, e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
//                logger.error("error to close httpClient source.");
            }
            try {
                httpClient.close();
            } catch (Exception e) {
//                logger.error("error to close httpClient source.");
            }
        }

        return body;

    }
}
