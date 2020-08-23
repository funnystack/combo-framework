package com.funny.combo.core.base;


import java.io.Serializable;

/**
 * 分页基础
 *
 * @author LiLong 2014-7-29
 */
public class BasePage implements Cloneable, Serializable {

    protected Integer pageNo;

    protected Integer pageSize;

    public Integer getPageNo() {
        return pageNo == null ? 0 : pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize == null ? 20 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
