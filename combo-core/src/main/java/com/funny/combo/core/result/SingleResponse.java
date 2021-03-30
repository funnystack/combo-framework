package com.funny.combo.core.result;

/**
 * Response with single record to return
 * <p/>
 * Created by Danny.Lee on 2017/11/1.
 */

public class SingleResponse<T> extends Response {

    private T data;

    public static <T> SingleResponse<T> of(T data) {
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setSuccess(true);
        singleResponse.setData(data);
        return singleResponse;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static SingleResponse buildFailure(Integer errCode, String errMessage) {
        SingleResponse response = new SingleResponse();
        response.setSuccess(false);
        response.setCode(errCode);
        response.setMessage(errMessage);
        return response;
    }

    public static SingleResponse buildSuccess(){
        SingleResponse response = new SingleResponse();
        response.setSuccess(true);
        return response;
    }

    public static SingleResponse fail(String message) {
        SingleResponse commonResult = new SingleResponse();
        commonResult.setCode(CODE_FAILURE);
        commonResult.setMessage(message);
        return commonResult;
    }

    public static <T> SingleResponse succ() {
        SingleResponse commonResult = new SingleResponse();
        commonResult.setCode(CODE_SUCCESS);
        return commonResult;
    }

    public static <T> SingleResponse succ(T data) {
        SingleResponse commonResult = succ();
        commonResult.setData(data);
        return commonResult;
    }

    public static <T> SingleResponse succ(T data, String msg) {
        SingleResponse commonResult = succ();
        commonResult.setData(data);
        commonResult.setMessage(msg);
        return commonResult;
    }


}
