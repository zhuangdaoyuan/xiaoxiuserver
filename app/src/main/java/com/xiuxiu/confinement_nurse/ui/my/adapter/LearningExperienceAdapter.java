package com.xiuxiu.confinement_nurse.ui.my.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.view.LearningExperienceView;

import org.jetbrains.annotations.NotNull;

public class LearningExperienceAdapter extends BaseQuickAdapter<LearningExperienceVm.LearningExperience, BaseViewHolder> {

    public LearningExperienceAdapter() {
        super(R.layout.item_learning_experience);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, LearningExperienceVm.LearningExperience orderRecommendItemVm) {
        ((LearningExperienceView)baseViewHolder.itemView).loadData(orderRecommendItemVm);
    }
}
