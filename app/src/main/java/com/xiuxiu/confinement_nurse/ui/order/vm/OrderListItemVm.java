package com.xiuxiu.confinement_nurse.ui.order.vm;

import java.io.Serializable;
import java.util.List;

public class OrderListItemVm implements Serializable {
    private List<OrderBean> items;
    private PageBean pageBean;

    public List<OrderBean> getItems() {
        return items;
    }

    public void setItems(List<OrderBean> items) {
        this.items = items;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

}
