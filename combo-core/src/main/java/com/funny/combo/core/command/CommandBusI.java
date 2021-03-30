package com.funny.combo.core.command;

import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.core.result.Response;

/**
 *
 * CommandBus
 *
 * @author fulan.zjf 2017年10月21日 下午11:00:58
 */
public interface CommandBusI {

    /**
     * Send command to CommandBus, then the command will be executed by CommandExecutor
     *
     * @param Command or Query
     * @return Response
     */
    Response send(AbstractCommand cmd);
}
