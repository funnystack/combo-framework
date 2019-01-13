package com.funny.autocode.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 自定义property文件加载器,就为了从配置文件中取出配置
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {
    
    public static ResourceBundle config = ResourceBundle.getBundle("config");

    private static Map<String,Object> properties = null;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        
        properties = new HashMap<String,Object>();
        for(Entry<Object, Object>  entry : props.entrySet()){
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            properties.put(key, value);
        }
    }
    
    public static String getProperty(String name){
        return properties.get(name).toString();
    }
    
}
