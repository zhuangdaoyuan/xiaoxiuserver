package com.xiuxiu.confinement_nurse.ui.order.adapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderBean;
import com.xiuxiu.confinement_nurse.ui.order.view.OrderListView;

import org.jetbrains.annotations.NotNull;

public class OrderListAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> implements LoadMoreModule {

    public OrderListAdapter() {
        super(R.layout.item_order_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, OrderBean orderRecommendItemVm) {
        ((OrderListView)baseViewHolder.itemView).loadData(orderRecommendItemVm);
    }
}
