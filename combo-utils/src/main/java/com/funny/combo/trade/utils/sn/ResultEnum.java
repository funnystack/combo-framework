package com.funny.combo.trade.utils.sn;

/**
 * Created by funny on 17/9/19.
 */
public enum ResultEnum {
    SUCCESS(0, "成功"),
    ERROR_404(404, "页面找不到"),
    ERROR_500(500, "服务器错误"),
    DEFAULT_ERROR(1000, "系统错误"),
    MISS_PARAM(1001, "缺少必要的请求参数"),
    PARAM_FORMAT_ERROR(1002, "请求参数格式错误"),
    MISS_APP_ID(1003, "缺少参数_appid"),
    APP_ID_NOT_EXISTS(1004, "_appid不存在"),
    APP_ID_STOPED(1005, "_appid已停用"),
    MISS_SIGN(1006, "缺少签名"),
    SIGN_ERROR(1007, "签名错误"),
    MISS_TIMESTAMP(1008, "缺少参数_timestamp"),
    REQUEST_EXPIRED(1009, "请求已过期"),
    VERIFY_SIGN_EXCEPTION(1010, "验证签名异常"),
    MISS_TOKEN(1011, "缺少token参数"),
    TOKEN_EXPIRE(1012, "token无效"),
    VERIFY_TOKEN_EXCEPTION(1013, "验证Token时异常"),
    ONLY_GET_METHOD(1050, "HTTP请求非get方式"),
    ONLY_POST_METHOD(1051, "HTTP请求非post方式"),
    NO_PERMISSION(1052, "没有接口访问权限"),
    IP_NOT_ALLOW(1053, "ip限制不能访问资源"),
    MISS_USER_INFO(2010, "无法获取用户信息"),
    RPC_ERROR(3000, "无法获取用户信息");

    private Integer code;

    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    ResultEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
