package com.funny.combo.encrypt.web.service.config;

import org.springframework.core.io.Resource;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class KeyConfig {
    private static final char VERSION_PREFIX = 'v';
    private final Map<String, SecurityKey> cacheSecurity = new HashMap<>();
    private final Map<String, Integer> consumerKeyVersionStore = new HashMap<>();
    private String salt;

    public KeyConfig() throws NoSuchAlgorithmException {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    public KeyConfig(Resource file) throws NoSuchAlgorithmException {

    }

    private void init() {

    }

    private void addSecurityKey(SecurityKey security) {
        this.cacheSecurity.put(security.getSecurityKey(), security);
        Integer version = this.consumerKeyVersionStore.getOrDefault(security.getConsumerId(), 0);
        if (version == null || version < security.getSequence()) {
            this.consumerKeyVersionStore.put(security.getConsumerId(), security.getSequence());
        }
    }

    public String getSalt() {
        return this.salt;
    }

    public boolean isExistsConsumerId(String consumerId) {
        return this.consumerKeyVersionStore.containsKey(consumerId.toLowerCase());
    }

    public SecurityKey getSecurityKey(String consumerId) {
        int version = this.consumerKeyVersionStore.getOrDefault(consumerId.toLowerCase(), 0);
        if (version == 0)
            return null;

        return getSecurityKey(consumerId, version);
    }

    public SecurityKey getSecurityKey(String consumerId, int version) {
        String key = consumerId + "." + VERSION_PREFIX + version;
        return this.cacheSecurity.getOrDefault(key, null);
    }

}
