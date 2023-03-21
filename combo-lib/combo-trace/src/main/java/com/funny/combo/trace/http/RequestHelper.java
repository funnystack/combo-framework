package com.funny.combo.trace.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author fangli
 * @desc RequestHelper
 * @date 16/4/23 下午5:52
 */
public class RequestHelper {

    private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);

    public static final String UTF8 = "UTF-8";
    // 设置连接超时时间,默认15s
    public static int CONNECTION_TIMEOUT = 15 * 1000;
    public static int maxTotal = 200;
    public static int maxPerRoute = 40;
    public static int maxRoute = 100;
    private static CloseableHttpClient httpClient = null;
    private final static Object syncLock = new Object();


    private static void config(HttpRequestBase httpRequestBase, Map<String, String> Headers) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(CONNECTION_TIMEOUT).build();
        httpRequestBase.setConfig(requestConfig);
        if (Headers != null) {
            for (String key : Headers.keySet()) {
                httpRequestBase.setHeader(key, Headers.get(key));
            }
        }
    }

    private static void setPostParamsToJson(HttpPost httppost, String params) {
        httppost.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
        StringEntity se = new StringEntity(params, ContentType.APPLICATION_JSON);
        httppost.setEntity(se);
    }

    /**
     * 获取HttpClient对象
     *
     * @param url
     * @return
     * @throws MalformedURLException
     */
    private static CloseableHttpClient getHttpClient(String url) throws MalformedURLException {
        URL purl = new URL(url);
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = createHttpClient(maxTotal, maxPerRoute, maxRoute, purl.getHost(), purl.getPort());
                }
            }
        }
        return httpClient;
    }

    /**
     * 创建HttpClient对象 此处解释下MaxtTotal和DefaultMaxPerRoute的区别： 1、MaxtTotal是整个池子的大小；
     * 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如： MaxtTotal=400 DefaultMaxPerRoute=200
     * 而我只连接到http://sishuok.com时，到这个主机的并发最多只有200；而不是400； 而我连接到http://sishuok.com 和
     * http://qq.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute。
     *
     * @param maxTotal
     * @param maxPerRoute
     * @param maxRoute
     * @param hostname
     * @param port
     * @return
     */
    private static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname,
                                                        int port) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf).register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加

        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, org.apache.http.protocol.HttpContext context) {
                // 如果已经重试了5次，就放弃
                if (executionCount >= 5) {
                    return false;
                }
                // 如果服务器丢掉了连接，那么就重试
                if (exception instanceof NoHttpResponseException) {
                    return true;
                }
                // 不要重试SSL握手异常
                if (exception instanceof SSLHandshakeException) {
                    return false;
                }
                // 超时
                if (exception instanceof InterruptedIOException) {
                    return false;
                }
                // 目标服务器不可达
                if (exception instanceof UnknownHostException) {
                    return false;
                }
                // 连接被拒绝
                if (exception instanceof ConnectTimeoutException) {
                    return false;
                }
                // SSL握手异常
                if (exception instanceof SSLException) {
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient =
                HttpClients.custom().setConnectionManager(cm).setRetryHandler(httpRequestRetryHandler).build();

        return httpClient;
    }

    private static void setPostParams(HttpPost httpost, Map<String, String> params)
            throws UnsupportedEncodingException {
        if (params == null || params.size() == 0) {
            return;
        }
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        httpost.setEntity(new UrlEncodedFormEntity(nvps, UTF8));
    }

    /**
     * 将传入的键/值对参数转换为NameValuePair参数集
     *
     * @param paramsMap 参数集, 键/值对
     * @return NameValuePair参数集
     */
    private static List getParamsList(Map<String, String> paramsMap) {
        if (paramsMap == null || paramsMap.size() == 0) {
            return null;
        }
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        for (Map.Entry<String, String> map : paramsMap.entrySet()) {
            params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
        }
        return params;
    }

    /**
     * 处理请求的后的response
     *
     * @param response
     * @return
     * @throws Exception
     */
    private static String handleReponse(CloseableHttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, UTF8);
        // 关闭流
        EntityUtils.consume(entity);
        return result;
    }

    private static String post(String url, HttpPost httppost, Map<String, String> params) throws Exception {
        setPostParams(httppost, params);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httppost, HttpClientContext.create());
            return handleReponse(response);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public static String post(String url, Map<String, String> params, Map<String, String> Headers) throws Exception {
        HttpPost httppost = new HttpPost(url);
        config(httppost, Headers);
        return post(url, httppost, params);
    }

    public static String post(String url, Map<String, String> params) throws Exception {
        return post(url, params, null);
    }

    public static String postJson(String url, String params) throws Exception {
        return postJsonWithHeader(url, params, null);
    }

    public static String postJsonWithHeader(String url, String params, Map<String, String> Headers) throws Exception {
        HttpPost httppost = new HttpPost(url);
        config(httppost, Headers);
        return postJson(url, httppost, params);
    }

    /**
     * postJson 请求
     *
     * @param url
     * @param httppost
     * @param params
     * @return
     * @throws Exception
     */
    private static String postJson(String url, HttpPost httppost, String params) throws Exception {
        setPostParamsToJson(httppost, params);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httppost, HttpClientContext.create());
            return handleReponse(response);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }


    /**
     * GET请求URL获取内容
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String get(String url) {
        return get(url, null, null);
    }

    /**
     * GET请求URL获取内容
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String get(String url, Map params) {
        return get(url, params, null);
    }

    /**
     * GET请求URL获取内容
     *
     * @param url
     * @return
     */
    public static String get(String url, Map params, Map<String, String> Headers) {
        List qparams = getParamsList(params);
        if (qparams != null && qparams.size() > 0) {
            String formatParams = URLEncodedUtils.format(qparams, UTF8);
            url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams)
                    : (url.substring(0, url.indexOf("?") + 1) + formatParams);
        }
        HttpGet httpget = new HttpGet(url);
        config(httpget, Headers);
        if (Headers != null) {
            for (String key : Headers.keySet()) {
                httpget.setHeader(key, Headers.get(key));
            }
        }

        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httpget, HttpClientContext.create());
            return handleReponse(response);
        } catch (Exception e) {
            logger.error("http请求失败,url={}", url, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                logger.error("http请求失败,url={}", url, e);
            }
        }
        return null;
    }


    /**
     * 发送post请求
     * @param url  路径
     * @param json  参数(json类型)
     * @return
     * @throws Exception
     */
    public static String send(String url, String json,String user,String pwd) throws Exception{
        String userMsg = user + ":" + pwd;
        String base64UserMsg = Base64.encodeBase64String(userMsg.getBytes());
        String authorization="Basic "+base64UserMsg;
        System.out.println(authorization);

        String body = "";
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        //装填参数
        StringEntity s = new StringEntity(json, "utf-8");
        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                "application/json"));
        httpPost.setEntity(s);

        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", authorization);

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }


}
