package com.funny.combo.core.command;

import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.core.exception.ExceptionHandlerFactory;
import com.funny.combo.core.result.Response;
import com.google.common.collect.FluentIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CommandInvocation{

    private static Logger logger = LoggerFactory.getLogger(CommandInvocation.class);

    private CommandExecutorI commandExecutor;
    private Iterable<CommandInterceptorI> preInterceptors;
    private Iterable<CommandInterceptorI> postInterceptors;

    @Autowired
    private CommandHub commandHub;


    public CommandInvocation() {

    }

    public CommandInvocation(CommandExecutorI commandExecutor, List<CommandInterceptorI> preInterceptors,
                             List<CommandInterceptorI> postInterceptors){
        this.commandExecutor = commandExecutor;
        this.preInterceptors = preInterceptors;
        this.postInterceptors = postInterceptors;
    }

    public Response invoke(AbstractCommand abstractCommand) {
        Response response = null;
        try {
            preIntercept(abstractCommand);
            response = commandExecutor.execute(abstractCommand);
        }
        catch(Exception e){
            response = getResponseInstance(abstractCommand);
            response.setSuccess(false);
            ExceptionHandlerFactory.getExceptionHandler().handleException(abstractCommand, response, e);
        }
        finally {
            //make sure post interceptors performs even though exception happens
            postIntercept(abstractCommand, response);
        }
        return response;
    }

    private void postIntercept(AbstractCommand abstractCommand, Response response) {
        try {
            for (CommandInterceptorI postInterceptor : FluentIterable.from(postInterceptors).toSet()) {
                postInterceptor.postIntercept(abstractCommand, response);
            }
        }
        catch(Exception e){
            logger.error("postInterceptor error:"+e.getMessage(), e);
        }
    }

    private void preIntercept(AbstractCommand abstractCommand) {
        for (CommandInterceptorI preInterceptor : FluentIterable.from(preInterceptors).toSet()) {
            preInterceptor.preIntercept(abstractCommand);
        }
    }

    private Response getResponseInstance(AbstractCommand cmd) {
        Class responseClz = commandHub.getResponseRepository().get(cmd.getClass());
        try {
            return (Response) responseClz.newInstance();
        } catch (Exception e) {
            return new Response();
        }
    }

    public CommandExecutorI getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(CommandExecutorI commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public Iterable<CommandInterceptorI> getPreInterceptors() {
        return preInterceptors;
    }

    public void setPreInterceptors(Iterable<CommandInterceptorI> preInterceptors) {
        this.preInterceptors = preInterceptors;
    }

    public Iterable<CommandInterceptorI> getPostInterceptors() {
        return postInterceptors;
    }

    public void setPostInterceptors(Iterable<CommandInterceptorI> postInterceptors) {
        this.postInterceptors = postInterceptors;
    }

    public CommandHub getCommandHub() {
        return commandHub;
    }

    public void setCommandHub(CommandHub commandHub) {
        this.commandHub = commandHub;
    }
}
