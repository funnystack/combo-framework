package com.funny.combo.core.filter;

/**
 *
 */
public interface Filter<T> {

	void doFilter(T context, FilterInvoker nextFilter);

}
