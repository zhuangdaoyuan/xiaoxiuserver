package com.xiuxiu.confinement_nurse.ui.my.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.my.vm.OtherExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.view.OtherExperienceView;

import org.jetbrains.annotations.NotNull;


public class OtherExperienceAdapter extends BaseQuickAdapter<OtherExperienceVm.OtherExperience, BaseViewHolder> {

    public OtherExperienceAdapter() {
        super(R.layout.item_other_experience);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, OtherExperienceVm.OtherExperience orderRecommendItemVm) {
        ((OtherExperienceView)baseViewHolder.itemView).loadData(orderRecommendItemVm);
    }
}
