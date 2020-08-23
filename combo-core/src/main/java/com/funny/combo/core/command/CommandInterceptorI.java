package com.funny.combo.core.command;


import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.core.result.Response;

/**
 * Interceptor will do AOP processing before or after Command Execution
 *
 * @author fulan.zjf 2017年10月25日 下午4:04:43
 */
public interface CommandInterceptorI {

    /**
     * Pre-processing before command execution
     *
     * @param abstractCommand
     */
    default void preIntercept(AbstractCommand abstractCommand) {
    }

    ;

    /**
     * Post-processing after command execution
     *
     * @param abstractCommand
     * @param response, Note that response could be null, check it before use
     */
    default void postIntercept(AbstractCommand abstractCommand, Response response) {
    }

    ;

}
