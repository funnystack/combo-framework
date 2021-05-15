package com.funny.combo.encrypt.service;

import com.funny.combo.encrypt.domain.EncryptEntity;
import com.funny.combo.encrypt.domain.EncryptParam;
import com.funny.combo.encrypt.domain.EncryptResult;
import com.funny.combo.encrypt.exception.EncryptException;
import com.funny.combo.encrypt.utils.AesHelper;
import com.funny.combo.encrypt.utils.MD5Util;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

public class LocalEncryptService extends AbstractEncryptService {

    @Override
    public EncryptResult<List<EncryptEntity>> encrypt(EncryptParam encryptParam) {
        try {
            checkParam(encryptParam);
            List<EncryptEntity> encryptEntityList = encryptParam.getTextList().stream().map(
                    t -> {
                        EncryptEntity encryptEntity = new EncryptEntity();
                        try {
                            encryptEntity.setEncrypted(getEncryptText(encryptParam.getAppKey(), t));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            encryptEntity.setHash(getHash(t));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        encryptEntity.setOrigin(t);

                        return encryptEntity;
                    }
            ).collect(Collectors.toList());

            return EncryptResult.succ(encryptEntityList);

        } catch (EncryptException e) {
            return EncryptResult.fail(e.getErrorCode(), e.getMessage());
        }
    }

    @Override
    public EncryptResult<List<EncryptEntity>> decrypt(EncryptParam encryptParam) {
        try {
            checkParam(encryptParam);
            List<EncryptEntity> encryptEntityList = encryptParam.getTextList().stream().map(
                    t -> {
                        EncryptEntity encryptEntity = new EncryptEntity();
                        try {
                            encryptEntity.setOrigin(getDecryptText(encryptParam.getAppKey(), t));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            encryptEntity.setHash(getHash(encryptEntity.getOrigin()));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        encryptEntity.setEncrypted(t);

                        return encryptEntity;
                    }
            ).collect(Collectors.toList());

            return EncryptResult.succ(encryptEntityList);

        } catch (EncryptException e) {
            return EncryptResult.fail(e.getErrorCode(), e.getMessage());
        }
    }


    private String getHash(String origin) throws NoSuchAlgorithmException {
        return MD5Util.md5(origin);
    }

    private String getEncryptText(String appKey, String origin) throws Exception {
        return AesHelper.encrypt2(origin,appKey);
    }

    private String getDecryptText(String appKey, String encryptText) throws Exception {
        return AesHelper.decrypt2(encryptText,appKey);
    }
}
