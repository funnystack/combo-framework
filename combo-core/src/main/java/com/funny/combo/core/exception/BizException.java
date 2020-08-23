package com.funny.combo.core.exception;


import com.funny.combo.core.common.BasicErrorCode;
import com.funny.combo.core.common.ErrorCodeI;

/**
 * BizException is known Exception, no need retry
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BizException(String errMessage){
        super(errMessage);
        this.setErrCode(BasicErrorCode.BIZ_ERROR);
    }

    public BizException(ErrorCodeI errCode, String errMessage){
    	super(errMessage);
    	this.setErrCode(errCode);
    }

    public BizException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.BIZ_ERROR);
    }
}