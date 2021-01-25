package com.xiuxiu.confinement_nurse.ui.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutNewsBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.OrderHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.ui.news.vm.NewsItemVm;

public class NewsItemView extends FrameLayout {

    private LayoutNewsBinding bind;

    public NewsItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NewsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NewsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_news, this);
        bind = LayoutNewsBinding.bind(inflate);
        initView();
        initViewState();
        setListener();

    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    public void loadData(NewsItemVm orderRecommendItemVm) {
        Glide.with(bind.ivLayoutNewsHeadPortrait).load(orderRecommendItemVm.getUserAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.color.color_backdrop_placeholder)
                .error(R.color.color_backdrop_placeholder)
                .into(bind.ivLayoutNewsHeadPortrait);


        bind.ivLayoutNewsNew.setText(orderRecommendItemVm.getNews());
        bind.ivLayoutNewsUserName.setText(orderRecommendItemVm.getUserName());
        bind.ivLayoutNewsUserTypeArea.setText(orderRecommendItemVm.getArea());
        bind.ivLayoutNewsUserType.setText(OrderHelper.userType(orderRecommendItemVm.getUserType()));
        bind.ivLayoutNewsUserTypeTime.setText(DateHelper.getTimeDescription(orderRecommendItemVm.getTime()));

        updateNum(orderRecommendItemVm.getNumberOfNews());
    }

    public void updateNum(int num) {
        if (num <= 0) {
            ViewHelper.hideView(bind.ivLayoutNewsNum);
            bind.ivLayoutNewsNum.setText("");
        } else {
            if (num > 999) {
                num = 999;
            }
            ViewHelper.showView(bind.ivLayoutNewsNum);
            bind.ivLayoutNewsNum.setText(String.valueOf(num));
        }

    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }


}
