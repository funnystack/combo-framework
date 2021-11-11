package com.funny.combo.encrypt.web.service;


import com.funny.combo.encrypt.web.service.config.SecurityKey;

/**
 */
public interface ICryptoService {
    String encryptOne(final SecurityKey key, final String mobile);

    String hashMobile(final String mobile);

    String decryptOne(final String consumerId, final String base64);
}
