package com.funny.combo.codegen.po;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public enum DatabaseEnum {
    LOCAL("本地测试", "jdbc:mysql://127.0.0.1:3306/trade", "123456", "root"),
    TEST("本地测试", "jdbc:mysql://127.0.0.1:3306/admin", "123456", "root");
    ;

    DatabaseEnum(String name, String url, String password, String username) {
        this.name = name;
        this.url = url;
        this.password = password;
        this.username = username;
    }

    private String name;
    private String url;
    private String password;
    private String username;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 将该枚举全部转化成json
     *
     * @return
     */
    public static JSONArray toJson() {
        JSONArray jsonArray = new JSONArray();
        for (DatabaseEnum e : DatabaseEnum.values()) {
            JSONObject object = new JSONObject();
            object.put("name", e.getName());
            object.put("url", e.getUrl());
            object.put("username", e.getUsername());
            object.put("password", e.getPassword());
            jsonArray.add(object);
        }
        return jsonArray;
    }
}
