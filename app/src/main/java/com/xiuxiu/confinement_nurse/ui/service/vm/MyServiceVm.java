package com.xiuxiu.confinement_nurse.ui.service.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyServiceVm implements Serializable {
    private List<MyServerInfoVm> items;

    public List<MyServerInfoVm> getItems() {
        return items==null?new ArrayList<>():items;
    }

    public void setItems(List<MyServerInfoVm> items) {
        this.items = items;
    }
}
