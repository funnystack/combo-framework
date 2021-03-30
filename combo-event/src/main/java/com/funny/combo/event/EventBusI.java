package com.funny.combo.event;


import com.funny.combo.core.result.Response;

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
     * @return Response
     */
     Response fire(EventI event);

    /**
     * fire all handlers which registed the event
     *
     * @param event
     * @return Response
     */
     void fireAll(EventI event);

    /**
     * Async fire all handlers
     * @param event
     */
     void asyncFire(EventI event);
}
