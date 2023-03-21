package com.funny.combo.core.extension;

import com.funny.combo.core.exception.BaseException;
import com.funny.combo.core.exception.BasicErrorCode;

/**
 *
 *
 * @author funnystack 2017/12/19
 */
public class ColaException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ColaException(String errMessage){
        super(errMessage);
        this.setErrCode(BasicErrorCode.COLA_ERROR);
    }

    public ColaException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.COLA_ERROR);
    }
}
