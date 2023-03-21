package com.funny.combo.core.result;

/**
 * @author funnystack 2017/12/19
 */
public class SingleResponse<T> extends Response {

    private T data;

    public static <T> SingleResponse<T> of(T data) {
        SingleResponse<T> singleResponse = new SingleResponse<>();
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
        response.setCode(errCode);
        response.setMessage(errMessage);
        return response;
    }

    public static SingleResponse build(){
        SingleResponse response = new SingleResponse();
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
