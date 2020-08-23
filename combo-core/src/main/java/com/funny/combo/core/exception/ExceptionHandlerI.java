package com.funny.combo.core.exception;

import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.core.result.Response;

/**
 * ExceptionHandlerI provide a backdoor that Application can override the default Exception handling
 *
 * @author Frank Zhang
 * @date 2019-01-02 11:25 PM
 */
public interface ExceptionHandlerI {
    void handleException(AbstractCommand cmd, Response response, Exception exception);
}
