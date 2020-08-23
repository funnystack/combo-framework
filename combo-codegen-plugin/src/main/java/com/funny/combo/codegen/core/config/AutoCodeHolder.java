package com.funny.combo.codegen.core.config;

public class AutoCodeHolder {


    private static ThreadLocal<GlobalConfig> threadLocal = new ThreadLocal<>();


    public static void setConfig(GlobalConfig globalConfig) {
        threadLocal.set(globalConfig);
    }


    public static GlobalConfig getConfig() {
        return threadLocal.get();
    }


}
