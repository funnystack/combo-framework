package com.funny.combo.trade.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: funnystack
 * @create: 2019-06-05 14:08
 */
public class SignUtil {

	private static final String appId = "214365";
	
	private static final String appKey = "4dc518a97e6c11bee4da681e313097db"; 
	
	public static final Logger logger = LoggerFactory.getLogger(SignUtil.class);
	/**
	 * 根据所有请求参数生成签名
	 * @param sMap 除签名外的所有请求参数按key做的升序排列
	 * @param appkey 申请
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSign(SortedMap<String,String> sMap,String appkey) throws NoSuchAlgorithmException{
		StringBuilder sb = new StringBuilder();
        sb.append(appkey);     
        for (Map.Entry<String, String> me:sMap.entrySet()){
           sb.append(me.getKey() + me.getValue());
        }
        sb.append(appkey);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(sb.toString().getBytes()); 
        byte b[] = md.digest(); 
        int i; 
        StringBuffer buf = new StringBuffer(""); 
        for (int offset = 0; offset < b.length; offset++) { 
            i = b[offset]; 
            if(i<0)i+= 256; 
            if(i<16)buf.append("0");
            buf.append(Integer.toHexString(i)); 
        }
		String sign = StringUtils.isEmpty(buf.toString()) == true ? "" : buf.toString().toUpperCase();
        return sign;
	}
	
	/**
	 * 一堆参数升序然后组合一下做md5
	 * @param sMap 除签名外的所有请求参数按key做的升序排列
	 * @return
	 */
	public static String createSign(Map<String,String> sMap){
		StringBuilder sb = new StringBuilder();
		sb.append(appKey);
        for (Map.Entry<String, String> me:sMap.entrySet()){
           sb.append(me.getKey() + me.getValue());
        }
        sb.append(appKey);
        MessageDigest md = null;
        StringBuffer buf = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(sb.toString().getBytes()); 
	        byte b[] = md.digest(); 
	        int i; 
	        buf = new StringBuffer(""); 
	        for (int offset = 0; offset < b.length; offset++) { 
	            i = b[offset]; 
	            if(i<0)i+= 256; 
	            if(i<16)buf.append("0");
	            buf.append(Integer.toHexString(i)); 
	        }
		} catch (NoSuchAlgorithmException e) {
			logger.error("SignUtil createSign fail. sMap:" + sMap,e);
		}
		String sign = StringUtils.isEmpty(buf.toString()) == true ? "" : buf.toString().toUpperCase();
        return sign;
	}
	
	
	public static void main(String args[]) throws NoSuchAlgorithmException{
		SortedMap<String, String> sMap = new TreeMap<String, String>();
		sMap.put("_appid", "mtest");
		sMap.put("_timestamp", "11111");
		sMap.put("orderId", "18201508051430");
		sMap.put("userId", "0730");
		sMap.put("operator", "zf");
		sMap.put("dealerId", "18612258145");
		
		getSign(sMap,"appKey");
		createSign(sMap);
	}

	/**
	 * 车服务接口用的签名，和上面咱们的有不同
	 * @param sMap
	 * @param appkey
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getCfwSign(SortedMap<String,String> sMap,String appkey){
		StringBuilder sb = new StringBuilder();
		sb.append(appkey);
		for (Map.Entry<String, String> me:sMap.entrySet()){
			sb.append(me.getKey() + me.getValue());
		}
		sb.append(appkey);
		String sign = string2MD5Utf8(sb.toString());
		return sign;
	}

	/***
	 * MD5加码 生成32位md5?? 使用utf-8字符集
	 */
	private static String string2MD5Utf8(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("UTF-8"));//这块和我们上面方法不一样
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();//这块和我们上面方法不一样

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 is unsupported", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MessageDigest不支持MD5Util", e);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		String sign = hex.toString();
		return sign;
	}

	/**
	 * 签名
	 * @param sortedMap
	 * @param sicret
	 * @return
	 */
	public static String generateSign(  SortedMap<String,String> sortedMap,String sicret){

		StringBuilder sb=new StringBuilder();
		sb.append(sicret);
		for (Map.Entry<String, String> me:sortedMap.entrySet())
			sb.append(me.getKey()+me.getValue());
		sb.append(sicret);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(sb.toString().getBytes());
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if(i<0)i+= 256;
			if(i<16)buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString().toUpperCase();
	}
}
