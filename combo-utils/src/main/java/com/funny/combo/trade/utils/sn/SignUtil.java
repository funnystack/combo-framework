package com.funny.combo.trade.utils.sn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 签名工具类
 * Created by funny on 16/8/26.
 */
public class SignUtil {
    /**
     * 传入的时间戳与本地时间允许相差的分钟数
     */
    private static final int TIMESTAMP_AVALIABLE_MINS = 10;
    /**
     * 签名参数名
     */
    public static final String SIGN_FIELD = "_sign";
    /**
     * 时间戳参数名 system.currentTimestamp()/1000
     */
    public static final String TIMESTAMP_FIELD = "_timestamp";

    public static final String APP_ID_FIELD = "_appid";

    public static void verifySign(final String appId, final String appKey, final Map<String, String> requestParams) throws APIException {
        //request对象直接提取的ParameterMap为Map<String, Object[]>类型,目的是当遇到相同参数名的参数时可以保存多个值
        //但在API中约定不可出现同名参数出现多次的情况,因此对于直接传入的map需要进行特殊处理
        APIParamDescriber describer = new APIParamDescriber(requestParams);
        //参数中的appId
        String paramAppId = describer.get(APP_ID_FIELD);
        if (StringUtils.isBlank(paramAppId)) {
            throw new APIException(ResultEnum.MISS_APP_ID);
        }
        if (StringUtils.isBlank(describer.get(TIMESTAMP_FIELD))) {
            throw new APIException(ResultEnum.MISS_TIMESTAMP);
        }
        //参数中的签名
        String paramSign = describer.get(SIGN_FIELD);
        if (StringUtils.isBlank((paramSign))) {
            throw new APIException(ResultEnum.MISS_SIGN);
        }
        if (!paramAppId.equals(appId)) {
            throw new APIException(ResultEnum.APP_ID_NOT_EXISTS);
        }
        long nowMin = TimeUnit.MINUTES.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);

        //参数中的时间戳
        long paramTimestamp = Long.parseLong(describer.get(TIMESTAMP_FIELD));
        long stampMin = TimeUnit.MINUTES.convert(paramTimestamp, TimeUnit.SECONDS);
        long dMin = stampMin - nowMin;
        if (Math.abs(dMin) > TIMESTAMP_AVALIABLE_MINS) {
            throw new APIException(ResultEnum.REQUEST_EXPIRED);
        }
        //去掉原始签名
        describer.remove(SIGN_FIELD);
        //重新计算签名
        String calcSign = getSign(appKey, describer);
        //对比签名
        if (!calcSign.equals(paramSign)) {
            throw new APIException(ResultEnum.SIGN_ERROR);
        }
        return;
    }

    /**
     * 计算签名
     *
     * @param appKey    服务接入的appKey
     * @param describer 待签名参数描述符
     * @return
     */
    private static String getSign(final String appKey, final APIParamDescriber describer) {
        StringBuilder sb = new StringBuilder();
        sb.append(appKey);
        for (Map.Entry<String, ?> entry : describer.entrySet()) {
            if (null == entry.getValue() || "".equals(entry.getValue())) {
                sb.append(entry.getKey());
            } else {
                sb.append(entry.getKey()).append(entry.getValue());
            }
        }
        sb.append(appKey);
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes()).toUpperCase();
    }
}
