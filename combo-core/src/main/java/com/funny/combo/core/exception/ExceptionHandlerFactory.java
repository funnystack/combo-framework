package com.funny.combo.core.exception;

import com.funny.combo.core.context.ApplicationContextHelper;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * ExceptionHandlerFactory
 *
 * @author Frank Zhang
 * @date 2019-01-08 9:51 AM
 */
public class ExceptionHandlerFactory {

    public static ExceptionHandlerI getExceptionHandler(){
        try {
            return ApplicationContextHelper.getBean(ExceptionHandlerI.class);
        }
        catch (NoSuchBeanDefinitionException ex){
            return DefaultExceptionHandler.singleton;
        }
    }

}
