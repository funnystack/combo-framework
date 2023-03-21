package com.funny.combo.core.exception;


/**
 * @author funnystack 2017/12/19
 */
public class SysException extends BaseException {

    private static final long serialVersionUID = 4355163994767354840L;

    public SysException(String errMessage) {
        super(errMessage);
        this.setErrCode(BasicErrorCode.SERVER_ERROR);
    }

    public SysException(ErrorCodeI errCode, String errMessage) {
        super(errMessage);
        this.setErrCode(errCode);
    }

    public SysException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.SERVER_ERROR);
    }

    public SysException(String errMessage, ErrorCodeI errorCode, Throwable e) {
        super(errMessage, e);
        this.setErrCode(errorCode);
    }
}
