package com.xiuxiu.confinement_nurse.ui.order.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;
import com.xiuxiu.confinement_nurse.ui.view.SwitchButton;

import org.jetbrains.annotations.NotNull;

public class FilterSwitchRadioAdapter extends BaseQuickAdapter<FilterRadioVm, BaseViewHolder> {
    private boolean radio;

    public boolean isRadio() {
        return radio;
    }

    public void setRadio(boolean radio) {
        this.radio = radio;
    }




    public FilterSwitchRadioAdapter() {
        super(R.layout.item_switch_radio);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, FilterRadioVm filterRadioVm) {
        SwitchButton switchButton = baseViewHolder.getView(R.id.item_switch_radio_sb);
        switchButton.setTitleLeft(filterRadioVm.getTitle());
        switchButton.setButtonSelectState(filterRadioVm.getState() == 1);
    }
}