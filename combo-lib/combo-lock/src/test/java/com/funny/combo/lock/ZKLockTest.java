package com.funny.combo.lock;

import com.funny.combo.lock.impl.ZKDistributedLock;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;

public class ZKLockTest {

    private CuratorFramework client;

    @Before
    public void set() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 60000, 15000, retryPolicy);
        client.start();
    }

    @Test
    public void testZKLock() {
        DistributedLock lock = new ZKDistributedLock(client, "balance/1233");
        boolean res = lock.lock();

        System.out.println(res);
        lock.releaseLock();
    }

}
