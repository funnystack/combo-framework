package com.funny.combo.trade.utils;

/**
 * @author: funnystack
 * @create: 2019-06-05 14:08
 */
public class BitCompute {
    /**
     * @param o
     * @param v
     * @return
     */
    public static int compute(Integer o, int v) {
        o = o == null ? 0 : o;
        if (v < 0) {
            return o;
        }
        return o | v;
    }

    /**
     * 指定位置是否存在
     *
     * @param o
     * @param v
     * @return
     */
    public static boolean exist(Integer o, int v) {
        o = o == null ? 0 : o;
        if (v <= 0) {
            return false;
        }
        return (o & v) == v;
    }

    /**
     * 重置某一位为0
     *
     * @param o
     * @param v
     * @return
     */
    public static int resetTheValue(Integer o, int v) {
        o = o == null ? 0 : o;
        if (v <= 0) {
            return o;
        }
        if (exist(o, v)) {
            return o ^ v;
        }

        return o;
    }

    public static void main(String[] args) {
        System.out.println(compute(15, 0));
        System.out.println(exist(15, 2));
        System.out.println(resetTheValue(15, 16));
    }
}
