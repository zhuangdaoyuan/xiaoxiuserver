package com.xiuxiu.confinement_nurse.ui.news.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.news.vm.NewsItemVm;
import com.xiuxiu.confinement_nurse.ui.news.view.NewsItemView;

import org.jetbrains.annotations.NotNull;

public class NewsAdapter extends BaseQuickAdapter<NewsItemVm, BaseViewHolder> {

    public NewsAdapter() {
        super(R.layout.item_news);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, NewsItemVm orderRecommendItemVm) {
        ((NewsItemView)baseViewHolder.itemView).loadData(orderRecommendItemVm);
    }
}
