package com.funny.combo.encrypt.web.service;


import com.funny.combo.encrypt.web.domain.DecryptOut;
import com.funny.combo.encrypt.web.domain.EncryptOut;
import com.funny.combo.encrypt.web.service.config.SecurityKey;

import java.util.List;

/**
 */
public interface IMobileService {
    List<EncryptOut> encrypt(SecurityKey key, String... inputs);

    List<EncryptOut> hash(String consumerId, String... inputs);

    List<DecryptOut> decrypt(String consumerId, String... ciphers);
}
