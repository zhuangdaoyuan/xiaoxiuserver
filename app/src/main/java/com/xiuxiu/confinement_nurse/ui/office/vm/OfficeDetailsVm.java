package com.xiuxiu.confinement_nurse.ui.office.vm;

import java.io.Serializable;

public class OfficeDetailsVm implements Serializable {
    private OfficeBean item;

    public OfficeBean getItem() {
        return item;
    }

    public void setItem(OfficeBean item) {
        this.item = item;
    }
}
