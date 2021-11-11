package com.funny.combo.encrypt.web.domain;

/**
 */
public class EncryptOut {
    private Integer status;
    private String error;
    private String mobile;
    private String encrypted;
    private String hash;

    public EncryptOut(String mobile) {
        this.mobile = mobile;
    }

    public EncryptOut(int status, String error) {
        this.status = status;
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}
