package com.funny.combo.core.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *@author funnystack 2017/12/19
 */
public  class AbstractDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * This is for extended values
     */
    protected Map<String, Object> extValues = new HashMap<String, Object>();

    public Object getExtField(String key){
        if(extValues != null){
            return extValues.get(key);
        }
        return null;
    }

    public void putExtField(String fieldName, Object value){
        this.extValues.put(fieldName, value);
    }

    public Map<String, Object> getExtValues() {
        return extValues;
    }

    public void setExtValues(Map<String, Object> extValues) {
        this.extValues = extValues;
    }
}
