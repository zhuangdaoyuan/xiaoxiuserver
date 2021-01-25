package com.xiuxiu.confinement_nurse.ui.office.adapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeBean;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeItemVm;
import com.xiuxiu.confinement_nurse.ui.view.OfficeItemView;

import org.jetbrains.annotations.NotNull;

public class OfficeItemAdapter extends BaseQuickAdapter<OfficeBean, BaseViewHolder> implements LoadMoreModule {

    public OfficeItemAdapter() {
        super(R.layout.item_order_recommend);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, OfficeBean orderRecommendItemVm) {
        ((OfficeItemView)baseViewHolder.itemView).loadData(orderRecommendItemVm);
    }
}
