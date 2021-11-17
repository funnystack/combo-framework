package com.funny.combo.core.exception;


/**
 * @author funnystack 2017/12/19
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BizException(String errMessage) {
        super(errMessage);
        this.setErrCode(BasicErrorCode.BIZ_ERROR);
    }

    public BizException(ErrorCodeI errCode, String errMessage) {
        super(errMessage);
        this.setErrCode(errCode);
    }

    public BizException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.BIZ_ERROR);
    }
}
