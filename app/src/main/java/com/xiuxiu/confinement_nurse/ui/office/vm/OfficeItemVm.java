package com.xiuxiu.confinement_nurse.ui.office.vm;

import com.xiuxiu.confinement_nurse.ui.order.vm.PageBean;

import java.io.Serializable;
import java.util.List;

public class OfficeItemVm implements Serializable {
    private PageBean pageBean;
    private List<OfficeBean> items;

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<OfficeBean> getItems() {
        return items;
    }

    public void setItems(List<OfficeBean> items) {
        this.items = items;
    }



}
