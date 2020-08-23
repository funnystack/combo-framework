package com.funny.combo.codegen.po;

import java.io.Serializable;

public class CodeURL implements Serializable {
    private String protocol;

    private String ip;

    private Integer port;

    private String dbName;

    private String url;
    private String usr;
    private String pas;

    public CodeURL(String protocol, String ip, Integer port, String dbName) {
        this.protocol = protocol;
        this.ip = ip;
        this.port = port;
        this.dbName = dbName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPas() {
        return pas;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
