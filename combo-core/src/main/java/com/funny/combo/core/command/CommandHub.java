package com.funny.combo.core.command;

import com.funny.combo.core.exception.ColaException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command Hub holds all the important information about Command
 *
 * @author fulan.zjf 2017-10-24
 */
@Component
public class CommandHub{

    /**
     * 全局通用的PreInterceptors
     */
    private List<CommandInterceptorI> globalPreInterceptors = new ArrayList<>();
    /**
     * 全局通用的PostInterceptors
     */
    private List<CommandInterceptorI> globalPostInterceptors = new ArrayList<>();
    private Map<Class/*CommandClz*/, CommandInvocation> commandRepository = new HashMap<>();

    /**
     * This Repository is used for return right response type on exception scenarios
     */
    private Map<Class/*CommandClz*/, Class/*ResponseClz*/> responseRepository = new HashMap<>();

    public CommandInvocation getCommandInvocation(Class cmdClass) {
        CommandInvocation commandInvocation = commandRepository.get(cmdClass);
        if (commandRepository.get(cmdClass) == null)
            throw new ColaException(cmdClass + " is not registered in CommandHub, please register first");
        return commandInvocation;
    }


    public List<CommandInterceptorI> getGlobalPreInterceptors() {
        return globalPreInterceptors;
    }

    public void setGlobalPreInterceptors(List<CommandInterceptorI> globalPreInterceptors) {
        this.globalPreInterceptors = globalPreInterceptors;
    }

    public List<CommandInterceptorI> getGlobalPostInterceptors() {
        return globalPostInterceptors;
    }

    public void setGlobalPostInterceptors(List<CommandInterceptorI> globalPostInterceptors) {
        this.globalPostInterceptors = globalPostInterceptors;
    }

    public Map<Class, CommandInvocation> getCommandRepository() {
        return commandRepository;
    }

    public void setCommandRepository(Map<Class, CommandInvocation> commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Map<Class, Class> getResponseRepository() {
        return responseRepository;
    }

    public void setResponseRepository(Map<Class, Class> responseRepository) {
        this.responseRepository = responseRepository;
    }
}
