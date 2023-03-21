package com.funny.combo.trade.utils;


import java.util.Base64;

/**
 * @description: base64加密解密工具类
 */
public class Base64Utils {

    /**
     * BASE64字符串解码为二进制数据
     *
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64.getDecoder().decode(base64.getBytes());
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
