package com.funny.combo.core.filter;

/**
 */
public interface FilterInvoker<T> {

    default public void invoke(T context){};
}
