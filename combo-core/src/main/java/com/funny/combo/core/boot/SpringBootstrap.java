package com.funny.combo.core.boot;

import com.funny.combo.core.common.ApplicationContextHelper;
import com.funny.combo.core.event.EventHandler;
import com.funny.combo.core.event.EventHandlerI;
import com.funny.combo.core.extension.Extension;
import com.funny.combo.core.extension.ExtensionPointI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * SpringBootstrap
 *
 * @author Frank Zhang
 * @date 2020-06-18 7:55 PM
 */
public class SpringBootstrap {

    @Autowired
    private ExtensionRegister extensionRegister;

    @Autowired
    private EventRegister eventRegister;

    public void init(){
       ApplicationContext applicationContext =  ApplicationContextHelper.getApplicationContext();
        Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(Extension.class);
        extensionBeans.values().forEach(
                extension -> extensionRegister.doRegistration((ExtensionPointI) extension)
        );

        Map<String, Object> eventHandlerBeans = applicationContext.getBeansWithAnnotation(EventHandler.class);
        eventHandlerBeans.values().forEach(
                eventHandler -> eventRegister.doRegistration((EventHandlerI) eventHandler)
        );
    }
}
