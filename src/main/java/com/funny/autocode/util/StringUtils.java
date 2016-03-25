package com.funny.autocode.util;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 针对一系列字符换的处理类
 * 
 * @author ECHO
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {

	private static final Logger LOG = LoggerFactory
			.getLogger(StringUtils.class);

	/**
	 * 转换对象为字符串，如果对象为null，则返回空格
	 * 
	 * @param obj
	 *            需要转换的对象
	 * @return 转换后的字符串
	 */
	public final static String toStringWithSpace(Object obj) {
		return obj == null ? " " : String.valueOf(obj);
	}

	/**
	 * 转换对象为字符串，如果对象为null，则返回空字符串
	 * 
	 * @param obj
	 *            需要转换的对象
	 * @return 转换后的字符串
	 */
	public final static String toStringWithOutNull(Object obj) {
		return obj == null ? "" : String.valueOf(obj);
	}

	/**
	 * 转换对象为字符串，如果对象为null，则返回“0”
	 * 
	 * @param obj
	 *            需要转换的对象
	 * @return 转换后的字符串
	 */
	public final static String toStringWithZero(Object obj) {
		return obj == null ? "0" : String.valueOf(obj);
	}

	/**
	 * 转换为BigDecimal
	 * 
	 * @param obj
	 * @return BigDecimal
	 */

	/**
	 * 将整数转换成字符串
	 * 
	 * @param number
	 *            数字
	 * @param length
	 *            字符串长度
	 * @param type
	 *            所需补齐的字符串
	 * @param location
	 *            新增字符串所在源字符串的位置1 ：原字符串前 2：原字符串后
	 * @return String
	 * */
	public final static String numberToString(int number, int length,
			String type, int location) {

		String numberStr = String.valueOf(number);

		if (numberStr.length() == length) {
			return numberStr;
		} else if (numberStr.length() > length) {
			return numberStr.substring(numberStr.length() - length);
		}

		StringBuffer value = new StringBuffer();
		for (int i = 0; i < length - numberStr.length(); i++) {
			value.append(type);
		}
		String resStr;
		if (location == 1) {
			resStr = numberStr + value.toString();
		} else {
			resStr = value.append(numberStr).toString();
		}
		return resStr;
	}

	/**
	 * 将元字符串补齐
	 * 
	 * @param str
	 *            原字符串长度
	 * @param length
	 *            字符串长度
	 * @param type
	 *            所需补齐的字符串
	 * @param location
	 *            新增字符串所在源字符串的位置1 ：原字符串后 2：原字符串前
	 * @return String
	 * */
	public final static String stringToString(String str, int length,
			String type, int location) {
		if (null == str || str.equals("")) {
			return str;
		}
		String numberStr = String.valueOf(str);

		if (numberStr.length() == length) {
			return numberStr;
		} else if (numberStr.length() > length) {
			return numberStr.substring(numberStr.length() - length);
		}

		StringBuffer value = new StringBuffer();
		for (int i = 0; i < length - numberStr.length(); i++) {
			value.append(type);
		}
		String resStr;
		if (location == 1) {
			resStr = numberStr + value.toString();
		} else {
			resStr = value.append(numberStr).toString();
		}
		return resStr;
	}

	private static String genStr(char padChar, int len) {
		StringBuilder str = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			str.append(padChar);
		}
		return str.toString();
	}

	/**
	 * 字符串截取方法,包含源，参考 org.apache.commons.lang。StringUtils
	 * 
	 * <pre>
	 *   * StringUtils.substringBetween("yabczyabcz", "a", "z")   = "abc"
	 * </pre>
	 * 
	 * @author Fu Wei
	 */

	public static String substringBetweenContainSrc(String str, String open,
			String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start, end);
			}
		}
		return null;
	}

	/**
	 * Double类型数据非科学计数法表示
	 * 
	 * @param obj
	 * @return 返回格式化后的Double数据类型
	 */
	public final static String formatDouble(Object obj) {
		DecimalFormat fmt = new DecimalFormat("#,##0.00");
		String str = String.valueOf(obj);
		if (obj instanceof Double) {
			return fmt.format(obj);
		} else if (str.matches("^[-\\+]?\\d+(\\.\\d+)?$")) {
			return fmt.format(Double.valueOf(str));
		} else {
			return toStringWithOutNull(str);
		}
	}

	/**
	 * 判断是否数值型
	 * 
	 * @param string
	 * @return true/false
	 */
	public final static boolean isNumber(String string) {
		if (string == null || string.equals("")) {
			return false;
		}
		if (string.indexOf(".") == 0
				|| string.indexOf(".") == string.length() - 1) {
			return false;
		}
		String validateStr = "0123456789.";
		for (int i = 0; i < string.length(); i++) {
			if (validateStr.indexOf(string.substring(i, i + 1)) == -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否整数型
	 * 
	 * @param string
	 * @return true/false
	 */
	public final static boolean isInteger(String string) {
		if (string == null || string.equals("")) {
			return false;
		}
		String validateStr = "0123456789";
		for (int i = 0; i < string.length(); i++) {
			if (validateStr.indexOf(string.substring(i, i + 1)) == -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将整数转换成字符串(支持数字前补零)
	 * 
	 * @param number
	 *            数字
	 * @param length
	 *            字符串长度
	 * @return String
	 * */
	public final static String numberToString(int number, int length) {

		String numberStr = String.valueOf(number);

		if (numberStr.length() == length) {
			return numberStr;
		} else if (numberStr.length() > length) {
			return numberStr.substring(numberStr.length() - length);
		}

		StringBuffer value = new StringBuffer();
		for (int i = 0; i < length - numberStr.length(); i++) {
			value.append("0");
		}
		value.append(numberStr);
		return value.toString();
	}

	/**
	 * 不同编码转换
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 */
	public final static String formatStringEncoding(String str, String encoding) {
		try {
			return new String(str.getBytes(), encoding);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return str;
	}

	/**
	 * 转换为BigDecimal
	 * 
	 * @param obj
	 * @return BigDecimal
	 */
	public final static BigDecimal parseBigDecimal(Object obj) {
		BigDecimal def = new BigDecimal("0");
		if (obj == null || "".equals(obj)) {
			return def;
		} else {
			// 去掉格式化的内容
			try {
				return new BigDecimal(String.valueOf(obj).replaceAll(",", ""));
			} catch (NumberFormatException e) {
				LOG.error("{} 数据转换错误: {}", obj, e);
				return def;
			}
		}
	}

	/**
	 * 将金额格式化为字符串
	 * 
	 * @param decimal
	 * @return 格式化后的金额，三位加逗号，保留两位小数
	 */
	public final static String parseBigDecimalToString(String decimal) {
		String[] str = decimal.split("\\.");
		// 小数值
		// String amount=str[1];
		int length = 2;// 设定小数点后的长度
		// 整数值
		String integral = str[0];

		char[] arrStr = integral.toCharArray();

		int j = 0;
		StringBuffer buffer = new StringBuffer();
		for (int i = arrStr.length - 1; i >= 0; i--) {
			buffer.insert(0, arrStr[i]);
			j++;
			if (j > 0 && j % 3 == 0 && i != 0) {
				buffer.insert(0, ",");
				j = 0;
			}
		}
		if (str.length == 2) {
			buffer.append(".");
			if (str[1].length() < length) {
				StringBuffer ss = new StringBuffer();
				for (int i = 0; i < length - str[1].length(); i++) {
					ss.append("0");
				}
				buffer.append(str[1]).append(ss);

			} else {
				buffer.append(str[1]);
			}

		}
		if (str.length == 1) {
			buffer.append(".00");
		}
		return buffer.toString();
	}

	/**
	 * 格式化交易参考号显示页面
	 * 
	 * @param tscNo
	 * @return
	 */
	public final static String formatTscNo(String tscNo) {
		return tscNo.substring(0, 3) + "-" + tscNo.substring(3, 7) + "-"
				+ tscNo.substring(7, 11) + "-" + tscNo.substring(11, 14);
	}

	/**
	 * 去掉交易参考号中的"-"
	 * 
	 * @param tscNo
	 * @return
	 */
	public final static String formatTscNoServer(String tscNo) {
		return tscNo.replaceAll("-", "");
	}

	/**
	 * 左补指定字符至指定位数
	 * 
	 * @param srcStr
	 *            元字符串
	 * @param padChar
	 *            补位字符
	 * @param destLen
	 *            目标长度
	 * @return
	 */
	public final static String leftPad(String srcStr, char padChar, int destLen) {

		if (srcStr == null) {
			return genStr(padChar, destLen);
		} else if (srcStr.length() < destLen) {
			StringBuilder destSrc = new StringBuilder(destLen);
			destSrc.append(srcStr);
			for (int i = srcStr.length(); i < destLen; i++) {
				destSrc.insert(0, padChar);
			}
			return destSrc.toString();
		} else {
			return srcStr;
		}
	}

	/**
	 * 右补指定字符至指定位数
	 * 
	 * @param srcStr
	 *            元字符串
	 * @param padChar
	 *            补位字符
	 * @param destLen
	 *            目标长度
	 * @return
	 */
	public final static String rightPad(String srcStr, char padChar, int destLen) {

		if (srcStr == null) {
			return genStr(padChar, destLen);
		} else if (srcStr.length() < destLen) {
			StringBuilder destSrc = new StringBuilder(destLen);
			destSrc.append(srcStr);
			for (int i = srcStr.length(); i < destLen; i++) {
				destSrc.append(padChar);
			}
			return destSrc.toString();
		} else {
			return srcStr;
		}

	}

	/**
	 * 隐藏证件号码后四位，用 *号代替
	 * 
	 * @param credNo
	 * @return
	 */
	public final static String hideCredNo(String credNo) {
		// 取证件号码长度
		if (credNo != null && !credNo.equals("")) {
			int len = credNo.length();
			// 后四位用*代替
			credNo = credNo.substring(0, len - 4).concat("****");
			// System.out.println(credNo);
			return credNo;
		} else {
			return toStringWithOutNull(credNo);
		}

	}
	
	/**
	 * 随机获取n位数字和字母字符串
	 * @param length 数字和字母字符串长度
	 * @return
	 */
	public static String getCharAndNumrRandom(int length) {
		String val = "";

		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

			if ("char".equalsIgnoreCase(charOrNum)) { // 字符串
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				val += String.valueOf(random.nextInt(10));
			}
		}

		return val;
	}
	
	/**
	 * 随机生成n位数字字符串
	 * @param length 生成数字字符串长度                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
	 * @return
	 */
	public static String getNumrRandom(int length) {
		String val = "";

		Random random = new Random();
		for (int i = 0; i < length; i++) {
			val += String.valueOf(random.nextInt(10));
		}

		return val;
	}
	 public static String toUtf8(String str) { 
		 String val = "";
		 
        try {
        	 val= new String(str.getBytes("UTF-8"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         return val;
   }
}