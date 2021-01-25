package com.xiuxiu.confinement_nurse.ui.order.vm;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;
import com.xiuxiu.confinement_nurse.ui.order.adapter.FilterAdapter;

import java.util.List;

public class FilterSwitchVm extends FilterVm implements MultiItemEntity {
    private boolean radio;

    public boolean isRadio() {
        return radio;
    }

    public void setRadio(boolean radio) {
        this.radio = radio;
    }

    private List<FilterRadioVm> list;

    public List<FilterRadioVm> getList() {
        return list;
    }

    public void setList(List<FilterRadioVm> list) {
        this.list = list;
    }

    @Override
    public int getItemType() {
        return FilterAdapter.KEY_SWITCH;
    }
}
