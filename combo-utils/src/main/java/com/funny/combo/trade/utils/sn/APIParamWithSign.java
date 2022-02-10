package com.funny.combo.trade.utils.sn;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * API基本的需要签名的公共参数定义
 * @author fangli
 * @since 2016年4月15日
 */
public abstract class APIParamWithSign extends APIParamWithoutSign {
	/** 时间戳,精确到秒
	 * system.currentTimestamp()/1000
	 */
	@JSONField(name = SignUtil.TIMESTAMP_FIELD)
	private Long timestamp;

	/** 签名 */
	@JSONField(name = SignUtil.SIGN_FIELD)
	private String sign;

	/**
	 * 获取时间戳,精确到秒
	 * @return 时间戳,精确到秒
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * 设置时间戳,精确到秒
	 * @param timestamp 时间戳,精确到秒
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 获取签名
	 * @return 签名
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * 设置签名
	 * @param sign 签名
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

}
