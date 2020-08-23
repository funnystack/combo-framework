package com.funny.combo.encrypt.exception;

/**
 * Created by funnystack on 20-9-16.
 */
public enum EncryptErrorEnum {
    SUCCESS(0," 成功"),


    ERROR_10000(10000,"encrypt response return null"),
    ERROR_10001(10001,"appKey is null"),
    ERROR_10002(10002,"timestamp is null"),
    ERROR_10003(10003,"timestamp is passed"),

    ;

    EncryptErrorEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public static EncryptErrorEnum getEnum(int code) {
        for (EncryptErrorEnum s : EncryptErrorEnum.values()) {
            if (code == s.getCode()) {
                return s;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
