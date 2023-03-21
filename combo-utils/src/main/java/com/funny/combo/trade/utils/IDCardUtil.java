package com.funny.combo.trade.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 身份证工具类 此身份证工具类有如下功能：
 * 1、验证身份证号码是否合法。（16位或18位）
 * 2、通过身份证号码计算年龄 。
 * 3、通过身份证号码计算出生日期
 * 4、通过身份证号码计算性别
 *
 * @author fangli
 */
public class IDCardUtil {

    private final static int[] wi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    private final static int[] vi = {1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};
    private static int[] ai = new int[18];

    /**
     * 验证身份证
     *
     * @param idCard
     * @return
     */
    public static boolean VerifyIDCard(String idCard) {
        if (idCard.length() == 15) {
            idCard = uptoeighteen(idCard);
        }
        if (idCard.length() != 18) {
            return false;
        }
        String verify = idCard.substring(17, 18);
        if (verify.toUpperCase().equals(getVerify(idCard))) {
            return true;
        }
        return false;
    }

    private static String getVerify(String eightcardid) {
        int remaining = 0;
        if (eightcardid.length() == 18) {
            eightcardid = eightcardid.substring(0, 17);
        }
        if (eightcardid.length() == 17) {
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                String k = eightcardid.substring(i, i + 1);
                ai[i] = Integer.parseInt(k);
            }
            for (int i = 0; i < 17; i++) {
                sum = sum + wi[i] * ai[i];
            }
            remaining = sum % 11;
        }
        return remaining == 2 ? "X" : String.valueOf(vi[remaining]);
    }

    private static String uptoeighteen(String fifteencardid) {
        String eightcardid = fifteencardid.substring(0, 6);
        eightcardid = eightcardid + "19";
        eightcardid = eightcardid + fifteencardid.substring(6, 15);
        eightcardid = eightcardid + getVerify(eightcardid);
        return eightcardid;
    }

    /**
     * 获取性别 0女，1男
     *
     * @param idcard
     * @return
     */
    public static String getSex(String idcard) {
        String inputStr = idcard.toString();
        int sex;
        if (inputStr.length() == 18) {
            sex = inputStr.charAt(16);
            if (sex % 2 == 0) {
                return "0";
            } else {
                return "1";
            }
        } else {
            sex = inputStr.charAt(14);
            if (sex % 2 == 0) {
                return "0";
            } else {
                return "1";
            }
        }
    }

    /**
     * 获得出生日期
     *
     * @param idcard
     * @return
     * @throws Exception
     */
    public static Date getBirthDate(String idcard) throws Exception {
        String year;
        String month;
        String day;
        if (idcard.length() == 18) { // 处理18位身份证
            year = idcard.substring(6, 10);
            month = idcard.substring(10, 12);
            day = idcard.substring(12, 14);
        } else { // 处理非18位身份证
            year = idcard.substring(6, 8);
            month = idcard.substring(8, 10);
            day = idcard.substring(10, 12);
            year = "19" + year;
        }
        return new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month
                + "-" + day);
    }

    /**
     * 根据身份证计算周岁
     *
     * @throws Exception
     */
    public static int getAge(String idcard) throws Exception {
        Calendar calBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        Date birthDate = getBirthDate(idcard);
        calBirth.setTime(birthDate);
        return getYearDiff(today, calBirth);
    }

    public static int getYearDiff(Calendar cal, Calendar cal1) {
        int m = (cal.get(cal.MONTH)) - (cal1.get(cal1.MONTH));
        int y = (cal.get(cal.YEAR)) - (cal1.get(cal1.YEAR));
        return (y * 12 + m) / 12;
    }
}
