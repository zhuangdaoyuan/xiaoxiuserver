package com.xiuxiu.confinement_nurse.ui.service.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.service.vm.MyServerInfoVm;
import com.xiuxiu.confinement_nurse.ui.service.view.MyServiceView;

import org.jetbrains.annotations.NotNull;

public class MyServerAdapter extends BaseQuickAdapter<MyServerInfoVm, BaseViewHolder> {

    public MyServerAdapter() {
        super(R.layout.item_my_server);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MyServerInfoVm orderRecommendItemVm) {
        ((MyServiceView)baseViewHolder.itemView).loadData(orderRecommendItemVm);
    }
}
