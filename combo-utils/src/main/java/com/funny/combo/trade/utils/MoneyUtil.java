package com.funny.combo.trade.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author: funnystack
 * @create: 2019-06-05 14:08
 */
public class MoneyUtil {

    /**
     * 元转为万的金额
     *
     * @param money
     * @return
     */

    private final static Integer wan_format = 10000;

    public static String getMoney2Wan(BigDecimal money) {
        StringBuffer sb = new StringBuffer();
        if (null == money) {
            return "";
        }

        String retMoney = formatDecimal(money.divide(new BigDecimal(wan_format))).toString();
        sb.append(retMoney);
        return sb.toString();
    }

    public static String format(BigDecimal money) {
//        StringBuffer sb = new StringBuffer();
//        if (null == money) {
//            return null;
//        }
//        if(money.doubleValue()>=10000) {
//            DecimalFormat df = new DecimalFormat();
//            df.setMaximumIntegerDigits(15);
//            df.setMaximumFractionDigits(4);
//            sb.append(df.format(money.divide(new BigDecimal(wan_format))));
//            sb.append("万");
//        }else {
//            sb.append(money.intValue());
//        }
//
//        return sb.toString();
        //return formatBreaks(money);
        return formatDecimal(money);
    }

    public static String format(Integer money) {
        BigDecimal decimal = new BigDecimal(money);
        return format(decimal);
    }

    public static String format(String money,int scale) {
        if (StringUtils.isBlank(money)) {
            return null;
        }
        BigDecimal decimal = new BigDecimal(money);
        return format(decimal,scale);
    }

    public static String format(BigDecimal mon,int scale) {
//        StringBuffer sb = new StringBuffer();
//        if(mon.doubleValue()>=10000) {
//            sb.append(mon.divide(new BigDecimal(wan_format),scale,BigDecimal.ROUND_HALF_UP));
//            sb.append("万");
//        }else {
//            sb.append(mon);
//        }
//
//        return sb.toString();

        return formatBreaks(mon);
    }

    public static String formatBreaks(BigDecimal mon){
        NumberFormat nf = new DecimalFormat(",###.##");
        return nf.format(mon);
    }

    public static String formatWithoutDot(BigDecimal mon){
        NumberFormat nf = new DecimalFormat("###");
        return nf.format(mon);
    }

    public static String formatDecimal(BigDecimal mon) {
        if (mon == null) {
            return null;
        }
        return mon.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static BigDecimal abs(BigDecimal mon){
        return mon.abs();
    }

    public static void main(String[] args) {
        System.out.println(MoneyUtil.formatDecimal(new BigDecimal("1500000.00")));
        System.out.println(MoneyUtil.formatWithoutDot(new BigDecimal("1500000")));
        System.out.println(MoneyUtil.formatDecimal(new BigDecimal("1500000.156")));
        System.out.println(MoneyUtil.formatDecimal(new BigDecimal("1500000.555")));
        System.out.println(MoneyUtil.format(new BigDecimal("1500000")));

        BigDecimal a = new BigDecimal("100.00");
        BigDecimal b = new BigDecimal("0.1");

        System.out.println(a.multiply(b).setScale(2));

        System.out.println("money2万：" + getMoney2Wan(new BigDecimal(286800.00)) + "万");
        System.out.println("--->" + getMoney2Wan(null));
    }

}
