package com.funny.combo.core.command;

import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.core.result.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CommandBus implements CommandBusI {

    @Autowired
    private CommandHub commandHub;

    @Override
    public Response send(AbstractCommand cmd) {
        return commandHub.getCommandInvocation(cmd.getClass()).invoke(cmd);
    }

}
