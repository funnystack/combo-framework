package com.funny.combo.trade.utils.sn;


/**
 * 调用API时产生的异常定义
 *
 * @author fangli
 * @since 2015年4月27日
 */
public class APIException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -889254804411836315L;

    /**
     * 错误代码
     */
    private ResultEnum errorCode;

    public APIException(ResultEnum errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
    }

    public APIException(ResultEnum errorCode, Throwable cause) {
        super(errorCode.getDesc(), cause);
        this.errorCode = errorCode;
    }

    public APIException(ResultEnum errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public APIException(int returnCode, String message) {
        super(message);
        this.errorCode.setCode(returnCode);
        this.errorCode.setDesc(message);
    }

    public APIException(int returnCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode.setCode(returnCode);
        this.errorCode.setDesc(message);
    }

    /**
     * 获取错误代码
     *
     * @return 错误代码
     */
    public int getErrorCode() {
        return errorCode.getCode();
    }

    /**
     * 获取错误提示
     *
     * @return
     */
    public String getReturnMessage() {
        return errorCode.getDesc();
    }

    public ResultEnum getReturnEnum() {
        return errorCode;
    }

    /**
     * 设置错误代码
     *
     * @param errorCode 错误代码
     */
    public void setErrorCode(ResultEnum errorCode) {
        this.errorCode = errorCode;
    }

}
