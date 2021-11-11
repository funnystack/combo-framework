package com.funny.combo.core.command;


import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.core.result.Response;

/**
 */
public interface CommandExecutorI<R extends Response, C extends AbstractCommand> {

    R execute(C cmd);
}
