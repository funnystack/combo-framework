package com.funny.combo.trade.utils;


import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * 获取tomcat端口号
 * @author fangli
 */
public class IPAddressKowalski{

    public static String getIpAddressAndPort() throws MalformedObjectNameException, NullPointerException,
            UnknownHostException {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = objectNames.iterator().next().getKeyProperty("port");
        String ipAdd = "http" + "://" + host + ":" + port;
        return ipAdd;
    }

    public static int getTomcatPort() throws MalformedObjectNameException, NullPointerException {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        String port = objectNames.iterator().next().getKeyProperty("port");

        return Integer.valueOf(port);
    }
}
