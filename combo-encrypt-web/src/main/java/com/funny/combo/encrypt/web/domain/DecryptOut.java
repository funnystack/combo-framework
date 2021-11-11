package com.funny.combo.encrypt.web.domain;

/**
 */
public class DecryptOut {
    private Integer status;
    private String error;
    private String mobile;
    private String encrypted;

    public DecryptOut(int status, String encrypted) {
        this.status = status;
        this.encrypted = encrypted;
    }

    public DecryptOut(int status, String encrypted, String error) {
        this.status = status;
        this.encrypted = encrypted;
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
}
