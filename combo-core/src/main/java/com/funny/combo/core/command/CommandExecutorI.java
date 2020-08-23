package com.funny.combo.core.command;


import com.funny.combo.core.dto.AbstractCommand;
import com.funny.combo.core.result.Response;

/**
 * CommandExecutorI
 *
 * @author fulan.zjf 2017年10月21日 下午11:01:05
 */
public interface CommandExecutorI<R extends Response, C extends AbstractCommand> {

    R execute(C cmd);
}
