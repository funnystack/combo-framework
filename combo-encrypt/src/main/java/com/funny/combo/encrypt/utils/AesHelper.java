package com.funny.combo.encrypt.utils;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author fangli
 * @since 1.0.0
 */
public class AesHelper {
    public static final String DefaultAppKey = "combo";
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     *
     */
    @Deprecated
    private static byte[] encryptBase(String value, String password) {
        verifyParam(value, password);
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = value.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            return cipher.doFinal(byteContent);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     */
    private static byte[] encryptBase2(String value, String password) {
        verifyParam(value, password);
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = value.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey2(password));
            return cipher.doFinal(byteContent);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     */
    @Deprecated
    public static String encrypt(String value, String password) {
        return Base64.getEncoder().encodeToString(encryptBase(value, password));
    }

    /**
     */
    public static String encrypt2(String value, String password) {
        return Base64.getEncoder().encodeToString(encryptBase2(value, password));
    }

    /**
     */
    @Deprecated
    public static String encryptHex(String value, String password) {
        return parseByte2HexString(encryptBase(value, password));
    }

    /**
     */
    public static String encryptHex2(String value, String password) {
        return parseByte2HexString(encryptBase2(value, password));
    }

    /**
     */
    @Deprecated
    private static byte[] decryptBase(byte[] value, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            return cipher.doFinal(value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     */
    private static byte[] decryptBase2(byte[] value, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey2(password));
            return cipher.doFinal(value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     */
    @Deprecated
    public static String decrypt(String value, String password) {
        verifyParam(value, password);

        byte[] bytes = decryptBase(Base64.getDecoder().decode(value), password);
        assert bytes != null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     */
    public static String decrypt2(String value, String password) {
        verifyParam(value, password);

        byte[] bytes = decryptBase2(Base64.getDecoder().decode(value), password);
        assert bytes != null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static void verifyParam(String value, String password) {
        if (value == null) {
            throw new IllegalArgumentException("value为null");
        }
        if (password == null) {
            throw new IllegalArgumentException("password为null");
        }
    }

    /**
     */
    @Deprecated
    public static String decryptHex(String value, String password) {
        verifyParam(value, password);
        byte[] bytes = decryptBase(parseHexString2Byte(value), password);
        assert bytes != null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     */
    public static String decryptHex2(String value, String password) {
        verifyParam(value, password);
        byte[] bytes = decryptBase2(parseHexString2Byte(value), password);
        assert bytes != null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     */
    @Deprecated
    private static SecretKeySpec getSecretKey(String password) {
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            kg.init(128, new SecureRandom(password.getBytes(StandardCharsets.UTF_8)));
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return
     */
    private static SecretKeySpec getSecretKey2(String password) {
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
            kg.init(128, secureRandom);

            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     */
    public static String parseByte2HexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     */
    public static byte[] parseHexString2Byte(String value) {
        if (value.length() < 1) {
            return null;
        }
        byte[] result = new byte[value.length() / 2];
        for (int i = 0; i < value.length() / 2; i++) {
            int high = Integer.parseInt(value.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(value.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {

        String str = "hello world";

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String encryptedString = encrypt2(str,DefaultAppKey);
            System.out.println("after encrypt:" + encryptedString);

            String decryptedString = decrypt2(encryptedString,DefaultAppKey);
            System.out.println("after decrypt:" + decryptedString);
        }
        System.out.println("10000 used" + (System.currentTimeMillis() - start));


    }

}
