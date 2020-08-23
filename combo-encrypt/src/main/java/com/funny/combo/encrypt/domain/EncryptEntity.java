package com.funny.combo.encrypt.domain;

import java.io.Serializable;

/**
 * Created by zhangpeng on 17/5/8.
 */
public class EncryptEntity implements Serializable {

    private static final long serialVersionUID = -2280877354850344161L;
    /**
     * 加密后密文
     */
    private String encrypted;

    /**
     * 待加密字符串hash值，固定长度64
     */
    private String hash;
    /**
     * 待加密明文
     */
    private String origin;

    public EncryptEntity() {
    }

    public EncryptEntity(String origin, String encrypted) {
        this.encrypted = encrypted;
        this.origin = origin;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
