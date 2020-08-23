package com.funny.combo.encrypt.service;

import com.funny.combo.encrypt.domain.EncryptEntity;
import com.funny.combo.encrypt.domain.EncryptParam;
import com.funny.combo.encrypt.domain.EncryptResult;

import java.util.List;

public class RpcEncryptService extends AbstractEncryptService{


    @Override
    public EncryptResult<List<EncryptEntity>> encrypt(EncryptParam encryptParam) {
        return null;
    }

    @Override
    public EncryptResult<List<EncryptEntity>> decrypt(EncryptParam encryptParam) {
        return null;
    }
}
