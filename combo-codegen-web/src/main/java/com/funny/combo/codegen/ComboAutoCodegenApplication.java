package com.funny.combo.codegen;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author fangli
 */
@SpringBootApplication(scanBasePackages = "com.funny.combo.codegen", exclude = {DataSourceAutoConfiguration.class})
public class ComboAutoCodegenApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        SerializeConfig config = SerializeConfig.globalInstance;
        config.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        return application.sources(ComboAutoCodegenApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ComboAutoCodegenApplication.class, args);
    }
}
