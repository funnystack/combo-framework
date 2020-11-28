package com.funny.combo.encrypt.utils;


import com.funny.combo.encrypt.domain.EncryptEntity;
import com.funny.combo.encrypt.domain.EncryptParam;
import com.funny.combo.encrypt.domain.EncryptResult;
import com.funny.combo.encrypt.exception.EncryptErrorEnum;
import com.funny.combo.encrypt.exception.EncryptException;
import com.funny.combo.encrypt.service.AbstractEncryptService;
import com.funny.combo.encrypt.service.EncryptHolder;
import com.funny.combo.encrypt.service.EncryptFactory;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感字段加解密组件
 * @since 2017/5/10
 */
public class EncryptUtils {

	/** 禁止私自实例化 */
	private EncryptUtils() {
	}

	private static volatile EncryptUtils instance = null;

	public static EncryptUtils getInstance() {
		if (instance == null) {
			synchronized (EncryptUtils.class) {
				if (instance == null) {
					instance = new EncryptUtils();
				}
			}
		}
		return instance;
	}

	/**
	 * 加密
	 * @param origin
	 * @return
	 * @throws EncryptException
	 */
	public EncryptEntity encryption(String origin) throws EncryptException {
		List<String> param = new ArrayList<>();
		param.add(origin);
		List<EncryptEntity> encryptResponses = getEncryptResponses(param);
		return CollectionUtils.isEmpty(encryptResponses) ? new EncryptEntity(origin,"") : encryptResponses.get(0);
	}

	/**
	 * 批量加密
	 * @param originalList
	 * @return
	 * @throws EncryptException
	 */
	public Map<String, EncryptEntity> encryption(List<String> originalList) throws EncryptException {
		Map<String, EncryptEntity> resultMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(originalList)) {
			List<EncryptEntity> encryptResponseList = getEncryptResponses(originalList);
			if (CollectionUtils.isNotEmpty(encryptResponseList)) {
				for (EncryptEntity en : encryptResponseList) {
					resultMap.put(en.getOrigin(), en);
				}
			}
		}
		return resultMap;
	}

	/**
	 * 解密
	 * @param encryptedText
	 * @return
	 * @throws EncryptException
	 */
	public EncryptEntity decryption(String encryptedText) throws EncryptException {
		List<String> param = new ArrayList<>();
		param.add(encryptedText);
		List<EncryptEntity> decryptionResponses = getDecryptionResponses(param);
		return CollectionUtils.isEmpty(decryptionResponses) ? new EncryptEntity("", encryptedText)	: decryptionResponses.get(0);
	}

	/**
	 * 批量解密
	 * @param cipherTextList
	 * @return
	 * @throws EncryptException
	 */
	public Map<String, String> decryption(List<String> cipherTextList) throws EncryptException {
		Map<String, String> resultMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(cipherTextList)) {
			List<EncryptEntity> encryptResponseList = getDecryptionResponses(cipherTextList);
			if (CollectionUtils.isNotEmpty(encryptResponseList)) {
				for (EncryptEntity en : encryptResponseList) {
					resultMap.put(en.getEncrypted(), en.getOrigin());
				}
			}
		}
		return resultMap;
	}

	/**
	 * 加密
	 * @param plainTextList 明文列表
	 * @return
	 * @throws EncryptException
	 */
	private List<EncryptEntity> getEncryptResponses(List<String> plainTextList) throws EncryptException {
		if (CollectionUtils.isEmpty(plainTextList)) {
			return null;
		}
		EncryptParam encryptParam = new EncryptParam();
		encryptParam.setAppKey(EncryptHolder.getAppId());
		encryptParam.setTimestamp(System.currentTimeMillis());
		encryptParam.setTextList(plainTextList);
		AbstractEncryptService abstractEncryptService = EncryptFactory.getEncryptService(EncryptHolder.getModel());
		EncryptResult<List<EncryptEntity>> encryptResult =  abstractEncryptService.encrypt(encryptParam);
		return requestAfterHandller(encryptResult);
	}

	/**
	 * 解密
	 *
	 * @param cipherTextList
	 * @return
	 * @throws EncryptException
	 */
	private List<EncryptEntity> getDecryptionResponses(List<String> cipherTextList) throws EncryptException {
		EncryptParam encryptParam = new EncryptParam();
		encryptParam.setAppKey(EncryptHolder.getAppId());
		encryptParam.setTimestamp(System.currentTimeMillis());
		encryptParam.setTextList(cipherTextList);
		AbstractEncryptService abstractEncryptService = EncryptFactory.getEncryptService(EncryptHolder.getModel());
		EncryptResult<List<EncryptEntity>> encryptResult = abstractEncryptService.decrypt(encryptParam);
		return requestAfterHandller(encryptResult);
	}

	/**
	 * 封装返回值
	 *
	 * @param encryptResult
	 */
	private List<EncryptEntity> requestAfterHandller(EncryptResult<List<EncryptEntity>> encryptResult) throws EncryptException {
		if(encryptResult == null ){
			throw new EncryptException(EncryptErrorEnum.ERROR_10000);
		}
		if(!encryptResult.getReturncode().equals(0)){
			throw new EncryptException(encryptResult.getReturncode(),encryptResult.getMessage());
		}
		return encryptResult.getResult();
	}

}
