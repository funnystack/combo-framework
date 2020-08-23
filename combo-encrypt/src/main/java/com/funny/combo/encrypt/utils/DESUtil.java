package com.funny.combo.encrypt.utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密DES
 * @author funnystack [2017年10月10日 下午3:58:10]
 */
public class DESUtil {
	private static final String DES = "DES";
	private static final String SHA1PRNG = "SHA1PRNG";
	private static  KeyGenerator keyGenerator = null;
	private static  SecureRandom random = null;

	static {
		try {
			keyGenerator = KeyGenerator.getInstance(DES);
			random = SecureRandom.getInstance(SHA1PRNG);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加密
	 * @param str
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String str, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.ENCRYPT_MODE, generateKey(key));
		byte[] bytes = cipher.doFinal(str.getBytes());
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	/**
	 * 解密
	 * @param str
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String str, String key) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(str);
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.DECRYPT_MODE, generateKey(key));
		return new String(cipher.doFinal(bytes));
	}

	/**
	 * 生成key
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key generateKey(String key) throws Exception {
		random.setSeed(key.getBytes());
		keyGenerator.init(56, random);
		//生成一个密钥
		SecretKey secretKey = keyGenerator.generateKey();
		return new SecretKeySpec(secretKey.getEncoded(), DES);
	}

	public static void main(String[] args) throws Exception {

		String str = "hello world";
		String key = "password";
		long start = System.currentTimeMillis();
		for(int i=0;i<1;i++) {
			// 加密
			String encryptedString = encrypt(str+i, key);
			System.out.println("加密后：" + encryptedString);
//
//			// 解密
//			String decryptedString = decrypt(encryptedString, key);
//			System.out.println("解密后：" + new String(decryptedString));
		}
		System.out.println("10000 used "  + (System.currentTimeMillis()-start));

	}
	
}