package com.funny.combo.core.filter;

/**
 * @author shawnzhan.zxy
 * @date 2018/04/17
 */
public interface FilterInvoker<T> {

    default public void invoke(T context){};
}
