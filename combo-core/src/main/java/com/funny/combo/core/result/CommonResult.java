package com.funny.combo.core.result;

import java.io.Serializable;

/**
 * @param <T>
 * @author fangli
 * @desc 服务返回处理结果
 * @time 19/12/3 下午4:32
 */
public class CommonResult<T> implements Serializable {

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_FAILURE = -1;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 返回编码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 业务对象
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void successBody(T data) {
        this.code = CODE_SUCCESS;
        this.message = "ok";
        this.data = data;
    }

    public static CommonResult fail(String message) {
        CommonResult commonResult = new CommonResult();
        commonResult.code = CODE_FAILURE;
        commonResult.message = message;
        return commonResult;
    }


    public static <T> CommonResult succ(T data) {
        CommonResult commonResult = succ();
        commonResult.setData(data);
        return commonResult;
    }

    public static <T> CommonResult succ(T data, String msg) {
        CommonResult commonResult = succ();
        commonResult.setData(data);
        commonResult.setMessage(msg);
        return commonResult;
    }

    public static <T> CommonResult succ() {
        CommonResult commonResult = new CommonResult();
        commonResult.code = CODE_SUCCESS;
        return commonResult;
    }

}
