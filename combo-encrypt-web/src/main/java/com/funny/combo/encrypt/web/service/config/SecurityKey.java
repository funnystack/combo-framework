package com.funny.combo.encrypt.web.service.config;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

/**
 *
 */
public class SecurityKey {
    private static final Joiner pointJoiner = Joiner.on(".").skipNulls();
    private String department;
    private String group;
    private String business;
    private char prefix;
    private Integer sequence;
    private String key;
    private SecretKeySpec keySpec;

    public String getConsumerId() {
        return pointJoiner.join(this.department, this.group, this.business);
    }

    public String getSecurityKey() {
        return getConsumerId() + "." + this.prefix + this.sequence;
    }

    public byte[] getVersion() {
        ByteBuffer buffer = ByteBuffer.allocate(6);
        buffer.putChar(this.prefix);
        buffer.putInt(this.sequence);
        return buffer.array();
    }

    /**
     * 获取密文中包含的version参数
     * @param bytes 加密字节数组
     * @return int version
     */
    public static int getVersion(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(ArrayUtils.subarray(bytes, bytes.length - 4, bytes.length));
        return buffer.getInt(0);
    }

    public byte[] encrypt(final String content) {
        try {
            Cipher cipher = CipherPool.getInstance();
            cipher.init(Cipher.ENCRYPT_MODE, getKeySpec());
            return cipher.doFinal(content.getBytes(Charsets.UTF_8));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String decrypt(final byte[] content) {
        try {
            Cipher cipher = CipherPool.getInstance();
            cipher.init(Cipher.DECRYPT_MODE, getKeySpec());
            byte[] bytes = cipher.doFinal(content);
            return new String(bytes, Charsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private SecretKeySpec getKeySpec() {
        if (this.keySpec == null)
            this.keySpec = new SecretKeySpec(this.key.getBytes(Charsets.UTF_8), "AES");
        return keySpec;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public char getPrefix() {
        return prefix;
    }

    public void setPrefix(char prefix) {
        this.prefix = prefix;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    static final class CipherPool {
        private static ThreadLocal<Cipher> ciphers = new ThreadLocal<Cipher>() {
            @Override
            protected Cipher initialValue() {
                try {
                    return Cipher.getInstance("AES/ECB/PKCS5PADDING");
                } catch(Exception e){
                    return null;
                }
            }
        };

        public static Cipher getInstance() {
            return ciphers.get();
        }

    }
}
