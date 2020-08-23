package com.funny.combo.encrypt.domain;

import java.io.Serializable;
import java.util.List;

public class EncryptParam implements Serializable {

    private String appKey;

    private Long timestamp;

    private List<String> textList;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }
}
