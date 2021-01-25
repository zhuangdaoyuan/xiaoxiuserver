package com.xiuxiu.confinement_nurse.ui.my.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.my.view.ServiceExperienceView;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderServiceVm;

import org.jetbrains.annotations.NotNull;

public class ServiceExperienceAdapter extends BaseQuickAdapter<OrderServiceVm, BaseViewHolder> {

    public ServiceExperienceAdapter() {
        super(R.layout.item_service_experience);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, OrderServiceVm orderRecommendItemVm) {
        ((ServiceExperienceView)baseViewHolder.itemView).loadData(orderRecommendItemVm);
    }
}
