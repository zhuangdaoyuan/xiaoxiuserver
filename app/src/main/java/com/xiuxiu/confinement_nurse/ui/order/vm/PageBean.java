package com.xiuxiu.confinement_nurse.ui.order.vm;

import java.io.Serializable;

/**
 * 分页数据
 */
public class PageBean implements Serializable {

    /**
     * pageNo : -3.809882221639325E7
     * pageSize : 1.0269748840093255E7
     * pages : -4.339101771335363E7
     * totalCount : 3.5442142821249604E7
     */

    private String pageNo;
    private int pageSize;
    private int pages;
    private int totalCount;

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}