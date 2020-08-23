package com.funny.combo.encrypt.autoconfig;

import com.funny.combo.encrypt.service.EncryptHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 相当于一个普通的 java 配置类
@Configuration
// 当 EncryptAppId 在类路径的条件下
@ConditionalOnClass({EncryptHolder.class})
// 将 application.properties 的相关的属性字段与该类一一对应，并生成 Bean
@EnableConfigurationProperties(EncryptProperties.class)
public class EncryptConfiguration {

  // 注入属性类
  @Autowired
  private EncryptProperties encryptProperties;

  @Bean
  // 当容器没有这个 Bean 的时候才创建这个 Bean
  @ConditionalOnMissingBean(EncryptHolder.class)
  public EncryptHolder helloworldService() {
    EncryptHolder encryptHolder = new EncryptHolder();
    encryptHolder.setAppId(encryptProperties.getAppId());
    encryptHolder.setModel(encryptProperties.getModel());
    return encryptHolder;
  }
}