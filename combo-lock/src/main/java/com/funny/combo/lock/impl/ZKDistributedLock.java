package com.funny.combo.lock.impl;

import com.funny.combo.lock.DistributedLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * @author fangli
 * @version 2021-10-12
 * 类说明     定义分布式锁需要的方法
 */
public class ZKDistributedLock extends DistributedLock {
    private static final String DS_LOCK_ROOT = "/combo_lock/";

    private CuratorFramework zkClient;
    private InterProcessMutex lock;
    private String lockPath = "";

    public ZKDistributedLock(CuratorFramework zkClient, String path) {
        this.zkClient = zkClient;
        this.lockPath = DS_LOCK_ROOT + path;
        lock = new InterProcessMutex(zkClient, lockPath);
    }

    @Override
    public boolean releaseLock() {
        boolean res = false;
        try {
            lock.release();
            deletePath(lockPath);
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public boolean lock(long time, TimeUnit unit) {
        try {
            return lock.acquire(time, unit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean lock() {
        try {
            lock.acquire();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void deletePath(String path) {
        try {
            zkClient.delete()
                    .deletingChildrenIfNeeded()
                    .forPath(path);
        } catch (Exception e) {
        }
    }
}
