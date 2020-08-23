package com.funny.combo.encrypt.service;

import com.funny.combo.encrypt.domain.EncryptEntity;
import com.funny.combo.encrypt.domain.EncryptParam;
import com.funny.combo.encrypt.domain.EncryptResult;
import com.funny.combo.encrypt.exception.EncryptErrorEnum;
import com.funny.combo.encrypt.exception.EncryptException;

import java.util.List;

public abstract class AbstractEncryptService {

    public abstract EncryptResult<List<EncryptEntity>> encrypt(EncryptParam encryptParam);

    public abstract EncryptResult<List<EncryptEntity>> decrypt(EncryptParam encryptParam);

    protected void checkAppKey(EncryptParam encryptParam) throws EncryptException {
        if(encryptParam.getAppKey() == null || encryptParam.getAppKey().equals("")){
            throw new EncryptException(EncryptErrorEnum.ERROR_10001);
        }
    }

    protected void checkTimeStamp(EncryptParam encryptParam) throws EncryptException {
        if(encryptParam.getTimestamp()== null){
            throw new EncryptException(EncryptErrorEnum.ERROR_10002);
        }
    }

    protected boolean checkParam(EncryptParam encryptParam) throws EncryptException {
        checkAppKey(encryptParam);
        checkTimeStamp(encryptParam);
        return true;
    }
}
