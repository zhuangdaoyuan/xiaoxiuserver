package com.xiuxiu.confinement_nurse.ui.order.vm;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xiuxiu.confinement_nurse.ui.order.adapter.FilterAdapter;

public class FilterTitleVm extends FilterVm implements MultiItemEntity {
    private String title;
    private String subtitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    @Override
    public int getItemType() {
        return FilterAdapter.KEY_TITLE;
    }
}
