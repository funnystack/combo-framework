package com.funny.combo.core.exception;

import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.core.result.Response;

/**
 *
 */
public interface ExceptionHandlerI {
    void handleException(AbstractCommand cmd, Response response, Exception exception);
}
