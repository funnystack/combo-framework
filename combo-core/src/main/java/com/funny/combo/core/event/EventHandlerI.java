package com.funny.combo.core.event;


import com.funny.combo.core.result.Response;

import java.util.concurrent.ExecutorService;

/**
 * event handler
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public interface EventHandlerI<R extends Response, E extends EventI> {

    default ExecutorService getExecutor(){
        return null;
    }

    R execute(E e);
}
