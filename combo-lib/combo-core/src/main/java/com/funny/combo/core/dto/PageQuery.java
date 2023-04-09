package com.funny.combo.core.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * page query
 * <p/>
 * @author funnystack 2017/12/19
 */
public abstract class PageQuery extends Query {

    private int pageNo = 1;
    private int pageSize = 10;
    private boolean needTotalCount = true;
    private List<OrderDesc> orderDescs;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setOrderDescs(List<OrderDesc> orderDescs) {
        this.orderDescs = orderDescs;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isNeedTotalCount() {
        return needTotalCount;
    }

    public void setNeedTotalCount(boolean needTotalCount) {
        this.needTotalCount = needTotalCount;
    }

    public List<OrderDesc> getOrderDescs() {
        return orderDescs;
    }

    public void addOrderDesc(OrderDesc orderDesc) {
        if (null == orderDescs) {
            orderDescs = new ArrayList<>();
        }
        orderDescs.add(orderDesc);
    }

    public int getOffset() {
        return pageNo > 0 ? (pageNo - 1) * pageSize : 0;
    }
}
