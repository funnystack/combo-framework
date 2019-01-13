package com.funny.autocode.common;

/**
 *
 * @author Administrator
 * @date 2017/8/8
 */
public enum URLEnum {
    ERP_MALL("jdbc:mysql://10.168.66.173:3306/erpmall", "sellmall","sellmall1234"),
    SELLER("jdbc:mysql://10.168.100.210:3306/mall", "popuser","popuser"),
    MALL_LEADS("jdbc:mysql://10.168.0.90:3306/automall_data", "root","123456");

    URLEnum(String url, String userName, String pwd) {
        this.url = url;
        this.userName = userName;
        this.pwd = pwd;
    }

    private String url;
    private String userName;
    private String pwd;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
