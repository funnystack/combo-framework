package com.funny.combo.trade.utils;

import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象copy工具类
 * @author fangli
 * @desc
 * @date 16/9/19 下午3:41
 */
public class CachedBeanCopier {

    private static final Map<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<String, BeanCopier>(10000);

    public static void copy(Object srcObj, Object destObj) {
        String key = genKey(srcObj.getClass(), destObj.getClass());
        BeanCopier copier = null;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        copier.copy(srcObj, destObj, null);
    }

    private static String genKey(Class<?> srcClazz, Class<?> destClazz) {
        return srcClazz.getName() + "_" + destClazz.getName();
    }
}
