package com.funny.combo.encrypt.exception;

public class EncryptException extends Exception {

    private static final long serialVersionUID = 2255351054794268138L;

    /**
     * 错误代码
     */
    private Integer errorCode;

    public EncryptException(EncryptErrorEnum encryptErrorEnum) {
        super(encryptErrorEnum.getMsg());
        this.errorCode = encryptErrorEnum.getCode();
    }

    public EncryptException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public EncryptException(Integer errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * 获取错误代码
     *
     * @return 错误代码
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * 设置错误代码
     *
     * @param errorCode 错误代码
     */
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

}
