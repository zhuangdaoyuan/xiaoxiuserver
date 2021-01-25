package com.xiuxiu.confinement_nurse.ui.my.adapter;

import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.my.view.LearningExperienceView;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;

import org.jetbrains.annotations.NotNull;

public class LocationAdapter extends BaseQuickAdapter<PoiInfo, BaseViewHolder> implements LoadMoreModule {

    public LocationAdapter() {
        super(R.layout.item_location);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PoiInfo cityInfo) {
        baseViewHolder.setText(R.id.title,cityInfo.getName());
        baseViewHolder.setText(R.id.subtitle,cityInfo.getAddress());
    }
}
