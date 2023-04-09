package com.funny.combo.core.result;

import com.funny.combo.core.dto.AbstractDTO;

/**
 *
 * @author funnystack 2017/12/19
 */
public class BaseResult extends AbstractDTO {

    private static final long serialVersionUID = 1L;

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_FAILURE = -1;

    private Integer code;

    private String msg;
    private String traceId;


    public boolean isSuccess() {
        return code == 0;
    }

    public Integer getCode() {
        return code;
    }

    public BaseResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public BaseResult setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public BaseResult setOK() {
        this.code = CODE_SUCCESS;
        return this;
    }

    @Override
    public String toString() {
        return "BaseResult:" + "errCode=" + code + ", errMessage=" + msg + "]";
    }

    public static BaseResult buildFailure(Integer errCode, String errMessage) {
        BaseResult response = new BaseResult();
        response.setCode(errCode);
        response.setMsg(errMessage);
        return response;
    }

    public static BaseResult build(){
        BaseResult response = new BaseResult();
        return response;
    }

}
