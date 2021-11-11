package com.funny.combo.encrypt.web.service.impl;

import com.funny.combo.encrypt.web.service.ICryptoService;
import com.funny.combo.encrypt.web.service.config.KeyConfig;
import com.funny.combo.encrypt.web.service.config.SecurityKey;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * on 2016/6/12.
 */
@Service
public class CryptoService implements ICryptoService {
    @Resource(name = "keyConfig")
    private KeyConfig keyConfig;

    @Override
    public String encryptOne(final SecurityKey key, final String mobile) {
        byte[] encrypted = key.encrypt(mobile);
        byte[] version = key.getVersion();
        byte[] bytes = ArrayUtils.addAll(encrypted, version);
        return BaseEncoding.base64().encode(bytes);
    }

    @Override
    @Cacheable(value = "HashMobileCache", key = "'sha256Mobile.'+#mobile")
    public String hashMobile(final String mobile) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(mobile);
        sb.append(Hashing.sha256().hashString(mobile, Charsets.UTF_8).toString());
        sb.append(keyConfig.getSalt());
        sb.append(Hashing.md5().hashString(mobile, Charsets.UTF_8).toString());
        return Hashing.sha256().hashString(sb, Charsets.UTF_8).toString();
    }

    /**
     * 解密
     */
    @Override
    @Cacheable(value = "DecryptCache", key = "'decrypt.'+#consumerId+'.'+#base64")
    public String decryptOne(final String consumerId, final String base64) {
        byte[] bytes = BaseEncoding.base64().decode(base64);
        SecurityKey key = parseDecryptKey(consumerId, bytes);
        if (key == null) {
            String error = String.format("CryptoService -> decrypt(%s, %s) base64 密文错误",
                    consumerId, base64);
            throw new IllegalArgumentException(error);
        }

        byte[] encrypted = ArrayUtils.subarray(bytes, 0, bytes.length - 6);
        return key.decrypt(encrypted);
    }

    private SecurityKey parseDecryptKey(String consumerId, byte[] bytes) {
        int version = SecurityKey.getVersion(bytes);
        return keyConfig.getSecurityKey(consumerId, version);
    }

}
