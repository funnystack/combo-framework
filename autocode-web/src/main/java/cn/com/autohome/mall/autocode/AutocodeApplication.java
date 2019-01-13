package cn.com.autohome.mall.autocode;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author fangli
 */
@SpringBootApplication(scanBasePackages = "cn.com.autohome.mall.autocode")
public class AutocodeApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        SerializeConfig config = SerializeConfig.globalInstance;
        config.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        return application.sources(AutocodeApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AutocodeApplication.class, args);
    }
}
