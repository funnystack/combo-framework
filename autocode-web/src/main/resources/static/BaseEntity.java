package cn.com.autohome.mall.base;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fangli
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
    protected Date created;

    /**
     * 修改时间
     */
    protected Date modified;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getModified() {
        return modified;
    }
}