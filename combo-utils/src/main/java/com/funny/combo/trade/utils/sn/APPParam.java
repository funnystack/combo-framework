package com.funny.combo.trade.utils.sn;


/**
 * API 手机基本的需要签名的公共参数定义
 *
 * @author fangli
 * @since 2016年4月15日
 */
public abstract class APPParam extends APIParamWithSign {
    /**
     * 设备所在经纬度 x,y
     */
    private String device_area;
    /**
     * IOS ANDROID
     */
    private String device_client;
    /**
     * 操作系统版本 号
     */
    private String device_client_version;
    /**
     * iPhone9 iPhone9
     */
    private String device_model;
    /**
     * WIFI 移动4G 联通4G 电信4G 3G
     */
    private String device_network;
    /**
     * 设备的操心系统版本号
     */
    private String device_os_version;
    /**
     * apple
     */
    private String device_partner;
    /**
     * 414*736
     */
    private String device_screen;
    /**
     * 设备唯一序列号
     */
    private String device_uuid;

    public String getDevice_area() {
        return device_area;
    }

    public void setDevice_area(String device_area) {
        this.device_area = device_area;
    }

    public String getDevice_client() {
        return device_client;
    }

    public void setDevice_client(String device_client) {
        this.device_client = device_client;
    }

    public String getDevice_client_version() {
        return device_client_version;
    }

    public void setDevice_client_version(String device_client_version) {
        this.device_client_version = device_client_version;
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public String getDevice_network() {
        return device_network;
    }

    public void setDevice_network(String device_network) {
        this.device_network = device_network;
    }

    public String getDevice_os_version() {
        return device_os_version;
    }

    public void setDevice_os_version(String device_os_version) {
        this.device_os_version = device_os_version;
    }

    public String getDevice_partner() {
        return device_partner;
    }

    public void setDevice_partner(String device_partner) {
        this.device_partner = device_partner;
    }

    public String getDevice_screen() {
        return device_screen;
    }

    public void setDevice_screen(String device_screen) {
        this.device_screen = device_screen;
    }

    public String getDevice_uuid() {
        return device_uuid;
    }

    public void setDevice_uuid(String device_uuid) {
        this.device_uuid = device_uuid;
    }
}
