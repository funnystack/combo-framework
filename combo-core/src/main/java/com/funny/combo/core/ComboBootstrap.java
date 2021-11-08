package com.funny.combo.core;

import com.funny.combo.core.event.EventHandler;
import com.funny.combo.core.event.EventHandlerI;
import com.funny.combo.core.event.EventRegister;
import com.funny.combo.core.extension.Extension;
import com.funny.combo.core.extension.ExtensionPointI;
import com.funny.combo.core.extension.ExtensionRegister;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * ExtensionBootstrap
 *
 * @author Frank Zhang
 * @date 2020-06-18 7:55 PM
 */
@Component
public class ComboBootstrap implements ApplicationContextAware {

    @Resource
    private ExtensionRegister extensionRegister;
    @Resource
    private EventRegister eventRegister;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(Extension.class);
        extensionBeans.values().forEach(
                extension -> extensionRegister.doRegistration((ExtensionPointI) extension)
        );
        Map<String, Object> eventBeans = applicationContext.getBeansWithAnnotation(EventHandler.class);
        eventBeans.values().forEach(
                event -> eventRegister.doRegistration((EventHandlerI) event)
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
