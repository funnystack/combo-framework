package com.funny.combo.core.result;

import com.funny.combo.core.dto.AbstractDTO;

/**
 *
 * @author fangli
 */
public class Response extends AbstractDTO {

    private static final long serialVersionUID = 1L;

    public static final int CODE_SUCCESS = 0;

    public static final int CODE_FAILURE = -1;

    private Integer code;

    private String message;

    public boolean isSuccess() {
        return code == 0;
    }

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

    @Override
    public String toString() {
        return "Response:" + "errCode=" + code + ", errMessage=" + message + "]";
    }

    public static Response buildFailure(Integer errCode, String errMessage) {
        Response response = new Response();
        response.setCode(errCode);
        response.setMessage(errMessage);
        return response;
    }

    public static Response buildSuccess(){
        Response response = new Response();
        return response;
    }

}
