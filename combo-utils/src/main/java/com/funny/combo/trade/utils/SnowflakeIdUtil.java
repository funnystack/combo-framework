package com.funny.combo.trade.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  @author: funnystack
 *  @create: 2019-06-05 14:08
 * 49 bits unique id:
 *
 * |--------|--------|--------|--------|--------|--------|--------|--------|
 * |00000000|00000001|11111111|11111111|11111111|11111111|11111111|11111111|
 * |--------|-------x|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxx--|--------|--------|
 * |--------|--------|--------|--------|--------|------xx|xxxxxxxx|--------|
 * |--------|--------|--------|--------|--------|--------|--------|xxxxxxxx|
 *
 * Maximum ID = ----1_11111111_11111111_11111111_11111111_11111111_11111111
 *
 * Maximum TS = ----1_11111111_11111111_11111111_111111-- -------- -------- = 约80年
 *
 * Maximum NT = ----- -------- -------- -------- ------11_11111111 -------- = 1024
 *
 * Maximum SH = ----- -------- -------- -------- -------- -------- 11111111 = 256
 *
 * It can generate 64k unique id per IP and up to 2088-02-07T06:28:15Z.
 */

public final class SnowflakeIdUtil {

    private static final Logger logger = LoggerFactory.getLogger(SnowflakeIdUtil.class);

    private static final Pattern PATTERN_LONG_ID = Pattern.compile("^([0-9]{15})([0-9a-f]{32})([0-9a-f]{3})$");

    private static final Pattern PATTERN_ADRESS = Pattern.compile("^.*\\D+([0-9]+)$");

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    private static final long OFFSET = LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.of("Z")).toEpochSecond();

    private static final long MAX_NEXT = 0b11111111_11L;

    private static final long WORKER_ID = getServerIdAsLong();

    private static long sequence = RandomUtils.nextLong(0,100);

    private static long lastEpoch = 0;

    /**
     * 前俩位拼接bucode,bizid.生成Long类型的ID数字,不超过17位
     * @return
     */
    public static long nextId(String bucode,String bizId) {
        Long snowflakeid = nextId();
        Long id = Long.parseLong(bucode+bizId+ snowflakeid.toString());
        return id;
    }

    /**
     * @return
     */
    public static long nextId() {
        return nextId(System.currentTimeMillis() / 1000);
    }

    public static synchronized long nextId(long epochSecond) {
        if (epochSecond < lastEpoch) {
            // warning: clock is turn back:
            logger.warn("clock is back: " + epochSecond + " from previous:" + lastEpoch);
            epochSecond = lastEpoch;
        }
        if (lastEpoch != epochSecond) {
            lastEpoch = epochSecond;
            reset();
        }
        sequence++;
        long next = sequence & MAX_NEXT;
        if (next == 0) {
            logger.warn("maximum id reached in 1 second in epoch: " + epochSecond);
            return nextId(epochSecond + 1);
        }
        return generateId(epochSecond, next, WORKER_ID);
    }

    private static void reset() {
        sequence = RandomUtils.nextLong(0,100);
    }

    private static long generateId(long epochSecond, long next, long workerId) {
        return ((epochSecond - OFFSET) << 18) | (next << 8) | workerId;
    }

    public static long getServerIdAsLong() {
        try {
            //String hostname = InetAddress.getLocalHost().getHostName();
            String address = getLocalAddress().getHostAddress();
            Matcher matcher = PATTERN_ADRESS.matcher(address);
            if (matcher.matches()) {
                long n = Long.parseLong(matcher.group(1));
                if (n >= 0 && n < 256) {
                    logger.info("detect server id from host name {}: {}.", address, n);
                    return n;
                }
            }
        } catch (Throwable e) {
            logger.warn("unable to get host name. set server id = 0.");
        }
        return 0;
    }

    /*public static long stringIdToLongId(String stringId) {
        // a stringId id is composed as timestamp (15) + uuid (32) + serverId (000~fff).
        Matcher matcher = PATTERN_LONG_ID.matcher(stringId);
        if (matcher.matches()) {
            long epoch = Long.parseLong(matcher.group(1)) / 1000;
            String uuid = matcher.group(2);
            byte[] sha1 = uuid.getBytes();
            //byte[] sha1 = HashUtil.sha1AsBytes(uuid);
            long next = ((sha1[0] << 24) | (sha1[1] << 16) | (sha1[2] << 8) | sha1[3]) & MAX_NEXT;
            long serverId = Long.parseLong(matcher.group(3), 16);
            return generateId(epoch, next, serverId);
        }
        throw new IllegalArgumentException("Invalid id: " + stringId);
    }*/

    /**
     * 获取IP地址,uncode编码,相加%32求余 占5位
     * @return
     */
    private static Long getWorkId(){
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for(int b : ints){
                sums += b;
            }
            return (long)(sums % 32);
        } catch (UnknownHostException e) {
            // 如果获取失败，则使用随机数备用
            return RandomUtils.nextLong(0,31);
        }
    }

    /**
     * 获取HostNmae名称,uncode编码,相加%32求余 占5位
     * @return
     */
    private static Long getDataCenterId(){
        int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
        int sums = 0;
        for (int i: ints) {
            sums += i;
        }
        return (long)(sums % 32);
    }

    /**
     * 获取本机网络地址,Docker可用
     * 支持Windows,Linux,IPv4和IPv6类型
     * @return
     */
    private static InetAddress getLocalAddress() {
        InetAddress localAddress = null;
        try {
            //如果能直接取到正确IP就返回，通常windows下可以
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
//            e.printStackTrace();
        }

        try {
            //通过轮询网卡接口来获取IP
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {
//                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Throwable e) {
//                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
//            e.printStackTrace();
        }

        return localAddress;
    }

    /**
     * 判断是否为有效合法的外部IP，而非内部回环IP
     * @param address
     * @return
     */
    private static boolean isValidAddress(InetAddress address) {
        if ((address == null) || (address.isLoopbackAddress())) {
            return false;
        }
        String ip = address.getHostAddress();

        return (ip != null) && (!"0.0.0.0".equals(ip)) && (!"127.0.0.1".equals(ip)) && (IP_PATTERN.matcher(ip).matches());
    }

}
