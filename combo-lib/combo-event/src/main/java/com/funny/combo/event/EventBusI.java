package com.funny.combo.event;


import com.funny.combo.core.result.BaseResult;

/**
 * EventBus interface
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public interface EventBusI {

    /**
     * Send event to EventBus
     *
     * @param event
     * @return BaseResult
     */
     BaseResult fire(EventI event);

    /**
     * fire all handlers which registed the event
     *
     * @param event
     * @return BaseResult
     */
     void fireAll(EventI event);

    /**
     * Async fire all handlers
     * @param event
     */
     void asyncFire(EventI event);
}
