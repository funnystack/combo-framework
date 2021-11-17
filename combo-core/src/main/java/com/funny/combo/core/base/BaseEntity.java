package com.funny.combo.core.base;

import java.io.Serializable;
import java.util.Date;

/**
 * @author funnystack 2017/12/19
 */
public class BaseEntity implements Cloneable, Serializable {
    private static final long serialVersionUID = 1490769462545L;

    /**
    * 主键id
    */
    protected Long id;

    /**
     * 创建时间
     */
    protected Date gmtCreated;

    /**
     * 修改时间
     */
    protected Date gmtModified;

    /**
     * 是否删除 0 正常 1删除
     */
    protected Integer isDel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}
