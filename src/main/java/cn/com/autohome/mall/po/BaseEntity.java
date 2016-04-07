package cn.com.autohome.mall.po;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author fangli@autohome.com.cn
 * 
 */
public class BaseEntity implements Cloneable,Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private Date createdTime;
    private Long createdBy;
    private Date modifyedTime;
    private Long modifyedBy;
    private Integer isValid;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public Long getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    public Date getModifyedTime() {
        return modifyedTime;
    }
    public void setModifyedTime(Date modifyedTime) {
        this.modifyedTime = modifyedTime;
    }
    public Long getModifyedBy() {
        return modifyedBy;
    }
    public void setModifyedBy(Long modifyedBy) {
        this.modifyedBy = modifyedBy;
    }
    public Integer getIsValid() {
        return isValid;
    }
    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
     
}
