package com.funny.combo.encrypt.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 摘要算法MD5
 * @author YangJie [2017年10月10日 下午3:58:10]
 */
public class MD5Util {
	private static final String MD5 = "MD5";
	private static final String ZERO = "0";

	/**
	 * md5
	 * @param str
	 * @return
	 */
	public final static String md5(String str) throws NoSuchAlgorithmException {
		MessageDigest messageDigest  = MessageDigest.getInstance(MD5);
		byte[] byteArray = messageDigest.digest(str.getBytes());
		messageDigest.update(str.getBytes());
		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append(ZERO).append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		long start = System.currentTimeMillis();
		String str = "hello world";
		for(int i=0;i<10000;i++) {
			System.out.println(md5(str));
		}
		System.out.println("10000 used "  + (System.currentTimeMillis()-start));
	}
	
}