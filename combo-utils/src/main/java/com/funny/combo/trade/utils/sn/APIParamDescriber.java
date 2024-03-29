package com.funny.combo.trade.utils.sn;

import java.util.Map;
import java.util.TreeMap;

/**
 * 为API参数特殊定制的TreeMap泛型类型,用于签名过程
 * @author fangli
 * @since 2015年4月16日
 */
public class APIParamDescriber extends TreeMap<String, String> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6048374400409228732L;

	public APIParamDescriber() {
	}

	public APIParamDescriber(Map m) {
		putAll(m);
	}
}
