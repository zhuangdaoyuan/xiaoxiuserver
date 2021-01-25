package com.xiuxiu.confinement_nurse.ui.my.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.my.vm.ServiceExperienceVm;

import org.jetbrains.annotations.NotNull;

public class SelfExpressionAdapter extends BaseQuickAdapter<ServiceExperienceVm.ServiceExperience, BaseViewHolder> {

    public SelfExpressionAdapter() {
        super(R.layout.item_self_experience);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ServiceExperienceVm.ServiceExperience orderRecommendItemVm) {
    }
}
