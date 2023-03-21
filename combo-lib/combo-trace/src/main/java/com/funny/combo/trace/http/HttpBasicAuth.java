package com.funny.combo.trace.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpBasicAuth {

    /**
     * 发送post请求
     *
     * @param url 路径
     * @return
     * @throws Exception
     */
    public static String sendGET(String url, String user, String pwd) throws Exception {
        String body = "";
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建get方式请求对象
        HttpGet httpGet = new HttpGet(url);
        configHeaderAuth(httpGet,user,pwd);

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpGet);
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

    /**
     * 发送post请求
     *
     * @param url  路径
     * @param json 参数(json类型)
     * @return
     * @throws Exception
     */
    public static String sendPost(String url, String json, String user, String pwd) throws Exception {
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
        configHeaderAuth(httpPost,user,pwd);

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

    private static void configHeaderAuth(HttpRequestBase httpRequestBase, String user, String pwd){
        String userMsg = user + ":" + pwd;
        String base64UserMsg = Base64.encodeBase64String(userMsg.getBytes());
        String authorization = "Basic " + base64UserMsg;
        httpRequestBase.setHeader("Authorization", authorization);

    }
}
