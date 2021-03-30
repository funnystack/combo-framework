package com.funny.combo.core.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Response with batch record to return,
 * usually use in page query or conditional query
 * <p/>
 * Created by Danny.Lee on 2017/11/1.
 */
public class PageResponse<T> extends Response {
    private Integer pageIndex;
    private Integer pageCount;
    private Integer pageSize;
    private Integer count;
    private int total;

    private Collection<T> data;

    public static <T> PageResponse<T> of(Collection<T> data, int total) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setSuccess(true);
        pageResponse.setData(data);
        pageResponse.setTotal(total);
        return pageResponse;
    }

    public static <T> PageResponse<T> ofWithoutTotal(Collection<T> data) {
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

    public static PageResponse buildFailure(Integer errCode, String errMessage) {
        PageResponse response = new PageResponse();
        response.setSuccess(false);
        response.setCode(errCode);
        response.setMessage(errMessage);
        return response;
    }

    public static PageResponse buildSuccess(){
        PageResponse response = new PageResponse();
        response.setSuccess(true);
        return response;
    }

}
