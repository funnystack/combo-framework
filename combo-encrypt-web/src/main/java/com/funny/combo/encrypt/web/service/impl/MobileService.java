package com.funny.combo.encrypt.web.service.impl;

import com.funny.combo.encrypt.web.domain.DecryptOut;
import com.funny.combo.encrypt.web.domain.EncryptOut;
import com.funny.combo.encrypt.web.service.ICryptoService;
import com.funny.combo.encrypt.web.service.IMobileService;
import com.funny.combo.encrypt.web.service.config.SecurityKey;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * on 2016/7/14.
 */
@Service
public class MobileService implements IMobileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobileService.class);
//    private static final Pattern BASE64_PATTERN = Pattern.compile("^(?:[A-Za-z0-9+\\/]{4})*(?:[A-Za-z0-9+\\/]{4}|[A-Za-z0-9+\\/]{3}=|[A-Za-z0-9+\\/]{2}==)=*$", Pattern.CASE_INSENSITIVE);
    private static final EncryptOut ENCRYPT_IS_EMPTY = new EncryptOut(102, "待加密值不能为空！");
    private static final DecryptOut DECRYPT_NOT_BASE64 = new DecryptOut(102, "", "解密字符不是base64值！");

    @Resource
    private ICryptoService cryptoService;

    /**
     * 加密
     */
    @Override
    public List<EncryptOut> encrypt(SecurityKey key, String... inputs) {
        List<EncryptOut> result = new ArrayList<>(inputs.length);
        for (String input : inputs) {
            result.add(doEncrypt(key, input));
        }

        return result;
    }

    private EncryptOut doEncrypt(SecurityKey key, String input) {
        if (Strings.isNullOrEmpty(input)) {
            return ENCRYPT_IS_EMPTY;
        }

        EncryptOut out = new EncryptOut(input);
        try {
            out.setStatus(0);
            out.setEncrypted(cryptoService.encryptOne(key, input));
            out.setHash(cryptoService.hashMobile(input));
        } catch (Exception e) {
            out.setStatus(500);
            out.setError(e.getMessage());
            LOGGER.error("CryptoService -> encryptOne(" + input + ") error", e);
        }

        return out;
    }

    @Override
    public List<EncryptOut> hash(String consumerId, String... inputs) {
        List<EncryptOut> result = new ArrayList<>(inputs.length);
        for (String input : inputs) {
            result.add(doHash(input));
        }
        return result;
    }

    private EncryptOut doHash(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return ENCRYPT_IS_EMPTY;
        }

        EncryptOut out = new EncryptOut(input);
        try {
            out.setStatus(0);
            out.setHash(cryptoService.hashMobile(input));
        } catch (Exception e) {
            out.setStatus(500);
            out.setError(e.getMessage());
            LOGGER.error("CryptoService -> hashMobile(" + input + ") error", e);
        }

        return out;
    }

    @Override
    public List<DecryptOut> decrypt(String consumerId, String... ciphers) {
        List<DecryptOut> result = new ArrayList<>(ciphers.length);
        for (String base64 : ciphers) {
            result.add(doDecrypt(consumerId, base64));
        }

        return result;
    }

    private DecryptOut doDecrypt(String consumerId, String base64) {
        if (Strings.isNullOrEmpty(base64)) {
            return DECRYPT_NOT_BASE64;
        }

        DecryptOut out = new DecryptOut(0, base64);
        try {
            String mobile = cryptoService.decryptOne(consumerId, base64);
            out.setMobile(mobile);
        } catch (Exception ex) {
            out.setStatus(500);
            out.setError(ex.getMessage());
            LOGGER.error("CryptoService -> decrypt(" + consumerId + ", " + base64 + ") error", ex);
        }
        return out;
    }


}
