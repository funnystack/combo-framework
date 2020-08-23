package com.funny.combo.core.domain;


import com.funny.combo.core.common.ApplicationContextHelper;

/**
 * DomainFactory
 *
 * @author Frank Zhang
 * @date 2019-01-03 2:41 PM
 */
public class DomainFactory {

    public static <T extends EntityObject> T create(Class<T> entityClz){
        return ApplicationContextHelper.getBean(entityClz);
    }

    public static <T > T getBean(Class<T> entityClz){
        return ApplicationContextHelper.getBean(entityClz);
    }
}
