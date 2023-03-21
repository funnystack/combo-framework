package com.funny.combo.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author fangli
 * @version 2021-10-12
 * 类说明     定义分布式锁需要的方法
 */
public abstract class DistributedLock {
    /**
     * 释放锁
     *
     * @throws InterruptedException
     */
    public abstract boolean releaseLock();
    /**
     * 尝试获得锁，如果有锁就返回，如果没有锁就等待，如果等待了一段时间后还没能获取到锁，那么就返回
     * @return
     */
    public abstract boolean lock(long time, TimeUnit unit);

    /**
     * 尝试获得锁，一直阻塞，直到获得锁为止
     */
    public abstract boolean lock();
}
