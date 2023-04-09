package com.funny.combo.core.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author funnystack 2017/12/19
 */
public class PageComboResult<T> extends BaseResult {
    private Integer pageIndex;
    private Integer pageCount;
    private Integer pageSize;
    private Integer count;
    private int total;

    private Collection<T> data;

    public static <T> PageComboResult<T> of(Collection<T> data, int total) {
        PageComboResult<T> pageResponse = new PageComboResult<>();
        pageResponse.setData(data);
        pageResponse.setTotal(total);
        return pageResponse;
    }

    public static <T> PageComboResult<T> ofWithoutTotal(Collection<T> data) {
        return of(data,0);
    }


    public int getTotal() {
        return total;
    }


    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return null == data ? new ArrayList<>() : new ArrayList<>(data);
    }


    public void setData(Collection<T> data) {
        this.data = data;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageCount() {
        if (pageCount == null && pageSize != null && pageSize > 0) {
            pageCount = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
        }
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public static PageComboResult buildFailure(Integer errCode, String errMessage) {
        PageComboResult response = new PageComboResult();
        response.setCode(errCode);
        response.setMsg(errMessage);
        return response;
    }

    public static PageComboResult build(){
        PageComboResult response = new PageComboResult();
        return response;
    }

    public static PageComboResult success(){
        PageComboResult response = new PageComboResult();
        return response;
    }

}
