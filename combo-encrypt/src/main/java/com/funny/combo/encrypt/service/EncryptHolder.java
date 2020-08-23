package com.funny.combo.encrypt.service;

public class EncryptHolder {
    private static String appId;
    /**
     * rpc  local
     */
    private static String model;

    public static String getAppId() {
        return appId;
    }

    public static void setAppId(String appId) {
        EncryptHolder.appId = appId;
    }

    public static String getModel() {
        return model;
    }

    public static void setModel(String model) {
        EncryptHolder.model = model;
    }
}
