package com.funny.combo.core.command;

import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.core.result.Response;

/**
 *
 */
public interface CommandBusI {

    /**
     * Send command to CommandBus, then the command will be executed by CommandExecutor
     *
     * @return Response
     */
    Response send(AbstractCommand cmd);
}
