package com.funny.combo.core.result;

/**
 * @author funnystack 2017/12/19
 */
public class ComboResult<T> extends BaseResult {

    private T data;

    public static <T> ComboResult<T> of(T data) {
        ComboResult<T> singleResponse = new ComboResult<>();
        singleResponse.setData(data);
        return singleResponse;
    }

    public T getData() {
        return data;
    }

    public ComboResult setData(T data) {
        this.data = data;
        return this;
    }

    public ComboResult setSuccessData(T data) {
        this.data = data;
        this.setOK();
        return this;
    }

    public static ComboResult buildFailure(Integer errCode, String errMessage) {
        ComboResult response = new ComboResult();
        response.setCode(errCode);
        response.setMsg(errMessage);
        return response;
    }

    public static ComboResult build(){
        ComboResult response = new ComboResult();
        return response;
    }

    public static ComboResult fail(String message) {
        ComboResult commonResult = new ComboResult();
        commonResult.setCode(CODE_FAILURE);
        commonResult.setMsg(message);
        return commonResult;
    }

    public static <T> ComboResult succ() {
        ComboResult commonResult = new ComboResult();
        commonResult.setCode(CODE_SUCCESS);
        return commonResult;
    }

    public static <T> ComboResult succ(T data) {
        ComboResult commonResult = succ();
        commonResult.setData(data);
        return commonResult;
    }

    public static <T> ComboResult succ(T data, String msg) {
        ComboResult commonResult = succ();
        commonResult.setData(data);
        commonResult.setMsg(msg);
        return commonResult;
    }


}
