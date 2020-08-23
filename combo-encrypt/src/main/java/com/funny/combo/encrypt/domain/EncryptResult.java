package com.funny.combo.encrypt.domain;

import java.io.Serializable;

/**
 * @param <T>
 * @author fangli
 * @desc 服务返回处理结果
 * @time 19/12/3 下午4:32
 */
public class EncryptResult<T> implements Serializable {

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_FAILURE = -1;
    /**
     * 返回编码
     */
    private Integer returncode;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 业务对象
     */
    private T result;


    public Integer getReturncode() {
        return returncode;
    }

    public void setReturncode(Integer returncode) {
        this.returncode = returncode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return returncode != null && returncode.equals(CODE_SUCCESS) ? true : false;
    }

    public void setSuccess() {
        this.returncode = CODE_SUCCESS;
    }


    public void successBody(T result) {
        this.returncode = CODE_SUCCESS;
        this.message = "ok";
        this.result = result;
    }

    public static EncryptResult fail(Integer code,String message) {
        EncryptResult jr = new EncryptResult();
        jr.returncode = code;
        jr.message = message;
        return jr;
    }

    public static EncryptResult failMsg(String message) {
        EncryptResult jr = new EncryptResult();
        jr.returncode = CODE_FAILURE;
        jr.message = message;
        return jr;
    }

    public static EncryptResult failBizMsg(String message) {
        EncryptResult jr = new EncryptResult();
        jr.returncode = CODE_FAILURE;
        return jr;
    }

    public static <T> EncryptResult succ(T result) {
        EncryptResult encryptResult = succ();
        encryptResult.setResult(result);
        return encryptResult;
    }

    public static <T> EncryptResult succ(T result, String msg) {
        EncryptResult encryptResult = succ();
        encryptResult.setResult(result);
        encryptResult.setMessage(msg);
        return encryptResult;
    }

    public static <T> EncryptResult succ() {
        EncryptResult jr = new EncryptResult();
        jr.returncode = CODE_SUCCESS;
        return jr;
    }

    public static <T> EncryptResult getInstance(Integer returncode, T result, String msg) {
        EncryptResult jr = new EncryptResult();
        jr.returncode = returncode;
        jr.message = msg;
        jr.result = result;
        return jr;
    }
}
