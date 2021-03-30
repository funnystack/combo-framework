package com.funny.combo.core.exception;


/**
 *
 * There are only 3 basic ErrorCode:
 * COLA_ERROR
 * BIZ_ERROR
 * SYS_ERROR
 *
 * Created by fulan.zjf on 2017/12/18.
 */
public enum BasicErrorCode implements ErrorCodeI {

    /**
     * 缺少参数
     */
    MISSING_PARAMS(101, "缺少参数"),
    /**
     * 格式不正确
     */
    INVALID_FORMAT(102, "格式不正确"),
    /**
     * 缺少APP ID
     */
    MISSING_APP_ID(103, "缺少APP ID"),
    /**
     * APP ID不存在
     */
    NOT_EXIST_APP_ID(104, "APP ID不存在"),
    /**
     * APP ID已禁用
     */
    DISABLE_APP_ID(105, "APP ID已禁用"),
    /**
     * 缺少签名
     */
    MISSING_SIGNATURE(106, "缺少签名"),
    /**
     * 签名不正确
     */
    INVALID_SIGNATURE(107, "签名不正确"),
    /**
     * 缺少时间戳
     */
    MISSING_TIMESTAMP(108, "缺少时间戳"),
    /**
     * 请求过期
     */
    REQUEST_EXPIRED(109, "请求过期"),
    /**
     * 重放的请求
     */
    REQUEST_RESEND(110, "重放的请求"),
    /**
     * 请求不是GET方式
     */
    REQUEST_IS_NOT_GET(111, "请求不是GET方式"),
    /**
     * 请求不是POST方式
     */
    REQUEST_IS_NOT_POST(112, "请求不是POST方式"),
    /**
     * 不存在的版本
     */
    NOT_EXIST_VERSION(113, "不存在的版本"),
    /**
     * 已禁用
     */
    DISABLED(115, "已禁用"),
    /**
     * 非法请求
     */
    ILLEGAL_REQUEST(116, "非法请求"),
    /**
     * 没有权限
     */
    NO_PERMISSION(117, "没有权限"),
    /**
     * 不支持的媒体类型
     */
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    /**
     * 服务器错误
     */
    SERVER_ERROR(500, "服务器错误"),

    /**
     * 调用服务接口失败
     */
    SERVER_INTERFACE(555, "调用服务接口失败"),

    BIZ_ERROR(555, "通用的业务逻辑错误"),

    COLA_ERROR(666 , "COLA框架错误");

    private Integer errCode;
    private String errDesc;

    private BasicErrorCode(Integer errCode, String errDesc){
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    @Override
    public Integer getErrCode() {
        return errCode;
    }

    @Override
    public String getErrDesc() {
        return errDesc;
    }
}
