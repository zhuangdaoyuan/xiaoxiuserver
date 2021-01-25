package com.xiuxiu.confinement_nurse.ui.schedule.vm;

import java.io.Serializable;
import java.util.List;

public class ScheduleListVm implements Serializable {
    private List<ScheduleVm> items;

    public List<ScheduleVm> getItems() {
        return items;
    }

    public void setItems(List<ScheduleVm> items) {
        this.items = items;
    }
}
