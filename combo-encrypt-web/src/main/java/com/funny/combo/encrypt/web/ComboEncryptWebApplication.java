package com.funny.combo.encrypt.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author fangli
 */
@SpringBootApplication(scanBasePackages = "com.funny.combo.encrypt.web")
public class ComboEncryptWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ComboEncryptWebApplication.class, args);
    }
}
