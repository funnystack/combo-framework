package com.funny.combo.trade.utils.sn;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * API最基本的无需签名的公共参数定义
 *
 * @author fangli
 * @since 2015年4月15日
 */
public abstract class APIParamWithoutSign {
    /**
     * API调用方标识
     */
    @JSONField(name = SignUtil.APP_ID_FIELD)
    private String appId;

    /**
     * 获取API调用方标识
     *
     * @return API调用方标识
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置API调用方标识
     *
     * @param appId API调用方标识
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

}
