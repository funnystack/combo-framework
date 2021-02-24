package com.funny.combo.oss.qiniu;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 七牛云工具
 *
 * @author Zero
 * @date 2019-05-28
 */
public class QiNiuUtil {
    private static final Logger logger = LoggerFactory.getLogger(QiNiuUtil.class);
    /**
     * 基础配置
     */
    private final static String accessKey = "y5ChTNd3OABtlu0JNRDR0A-w4UmwhDAhkPIdzz9f";
    private final static String secretKey = "Z0icRtZx2CpLxh6gmwWMvEVh1aCj0oQVR1zvBI_w";
    private final static String bucket = "stack";
    private final static String doMain = "http://82gcbe.s3-cn-south-1.qiniucs.com";

    /**
     * 七牛云上传文件
     *
     * @param file     文件地址
     * @param fileName 文件名
     * @return 返回结果
     */
    public static String fileUpload(File file, String fileName) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg;
        cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "file/" + System.currentTimeMillis();
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        String result;
        try {
            Response response = uploadManager.put(file, key + fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            logger.info("{七牛文件上传key: " + putRet.key + ",七牛excel文件传hash: " + putRet.hash + "}");
            result = doMain + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            result = null;
        }
        return result;
    }


	public static void main(String[] args) {
    	File file = new File("/Users/fangli/Pictures/cloud_wx_2.png");
		String result = fileUpload(file,"cloud_wx_2");
		//http://82gcbe.s3-cn-south-1.qiniucs.comfile/1614177236318cloud_wx_2
		System.out.println(result);
	}

}
