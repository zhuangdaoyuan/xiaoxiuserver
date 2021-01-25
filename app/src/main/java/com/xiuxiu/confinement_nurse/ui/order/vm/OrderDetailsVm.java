package com.xiuxiu.confinement_nurse.ui.order.vm;

import java.io.Serializable;
import java.util.List;

public class OrderDetailsVm implements Serializable {
    private OrderBean item;

    public OrderBean getItem() {
        return item;
    }

    public void setItem(OrderBean item) {
        this.item = item;
    }
}
