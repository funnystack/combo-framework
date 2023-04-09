package com.funny.combo.event;



import com.funny.combo.core.result.BaseResult;

import java.util.concurrent.ExecutorService;

/**
 * event handler
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public interface EventHandlerI<R extends BaseResult, E extends EventI> {

    default ExecutorService getExecutor(){
        return null;
    }

    R execute(E e);
}
