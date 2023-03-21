package com.funny.combo.trade.utils;

import com.alibaba.fastjson.JSON;

import java.util.Map;

import static com.funny.combo.trade.utils.RSAUtils.PRIVATE_KEY;
import static com.funny.combo.trade.utils.RSAUtils.PUBLIC_KEY;

public class KeyUtils {

    /**
     * 对指定内容执行公钥加密操作
     * 先rsa加密,然后用base64加密
     * @param content
     * @param publicKey
     * @return String
     */
    public static String encryptByPublicKey(String content, String publicKey) throws Exception {
        byte[] encryptData = RSAUtils.encryptByPublicKey(content.getBytes(), publicKey);
        return Base64Utils.encode(encryptData);
    }

    /**
     * 对指定内容执行私钥解密操作
     * 先base64解密,然后rsa解密
     * @param content
     * @param privateKey
     * @return String
     */
    public static String decryptByPrivateKey(String content, String privateKey) throws Exception {
        byte[] bytes = RSAUtils.decryptByPrivateKey(Base64Utils.decode(content), privateKey);
        return new String(bytes);
    }


    /**
     * 公钥
     */
    private static String publicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmXLOihPF4QwmBF0NKweX9ClJCAc967I/lHmtLeG1Hp0i2KqtX6YfFkITbg/8e35tCL7md/5yO2TY7hybQHJkwgk8L0vS86jogLLyusTGJvQWu2oC6vYlgbYQnn0La5ogeHb2hwdkNMEv6bYrbi3blRB7ycEBpJBI9XS19u8klvQIDAQAB";
    /**
     * 私钥
     */
    private static String privateKey ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKZcs6KE8XhDCYEXQ0rB5f0KUkIBz3rsj+Uea0t4bUenSLYqq1fph8WQhNuD/x7fm0IvuZ3/nI7ZNjuHJtAcmTCCTwvS9LzqOiAsvK6xMYm9Ba7agLq9iWBthCefQtrmiB4dvaHB2Q0wS/ptituLduVEHvJwQGkkEj1dLX27ySW9AgMBAAECgYAtx0CfLhoMT9YjsbUQsz6IXPi5tSnU15fZIn+/6smXeA7oIeG4dUipMtV8WlwvsLRWiC95rMb2AAuOYehqSBRW86wpwZGY+PTuo9WNMg2qxoixp8EUKStRAR1AnPrwR4/X2le05itO4qFIhKsRWULU8RbBLFnLiorHyztKs6SbgQJBAPSZNDrXmAWeo9Ienljfm+rULtu6XzRKxSDq9jYrlvIUlvLr5C8zTRS+H1q/4BUON0muHZ/RwhicfC2t7Xd1Gy0CQQCuHecSLNYoStyPQFTFwR/B9hqMZ/zZZjWY4YIfTzv4T4loUe+SIiwitMqqCWKSxM4Pn+ZJiKGz12LJMNY1Z47RAkEAgWCcANfSwH419sldRnMYfLC5DF5bR0SZWp0NmH+b+vYrFdjyPIktJ2CdIxpEw75ePinL36JKwUvcXykVZ09ZDQJARXl9CA2H7k/grfA3YyjqKRSwBzI+++gCntvsdd3ByhTCeTOoaiDkh3yoGSzhfjq1FhrxYon4K7BXpHuuHWUS4QJAM/ooGaCOhAZPlsOhSr5Lolf+kekpq1+IX421ZSpcK6J5fdnWIiZmsf/PfTTNL3RmOWz92Jj3kY92PHf5aAfX3g==";


    public static void main(String[] args) throws Exception {
        Map<String,Object> map = RSAUtils.genKeyPair();
        String publicKey = JSON.parseObject(JSON.toJSONString(map.get(PUBLIC_KEY))).getString("encoded");
        System.out.println(publicKey);
        String privateKey = JSON.parseObject(JSON.toJSONString(map.get(PRIVATE_KEY))).getString("encoded");
        System.out.println(privateKey);
        String rsaEnc = KeyUtils.encryptByPublicKey("funnystack",publicKey);
        System.out.println(rsaEnc);
        String origin = KeyUtils.decryptByPrivateKey(rsaEnc,privateKey);
        System.out.println(origin);
    }
}

