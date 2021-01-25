package com.xiuxiu.confinement_nurse.ui.order.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutOrderRecommendBinding;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeItemVm;

public class OrderItemInfoView extends LinearLayout {

    private  LayoutOrderRecommendBinding bind;

    public OrderItemInfoView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public OrderItemInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public OrderItemInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_order_recommend_info, this);
        bind = LayoutOrderRecommendBinding.bind(inflate);
        initView();
        initViewState();
        setListener();

    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    public void loadData(OfficeItemVm orderRecommendItemVm) {
//        bind.tvItemOrderRecommendTitle.setText( orderRecommendItemVm.getTitle());
//        bind.tvItemOrderRecommendPrice.setText( orderRecommendItemVm.getPrice());
//        bind.tvItemOrderRecommendTime.setText( orderRecommendItemVm.getTime());
//        bind.tvItemOrderRecommendUserName.setText( orderRecommendItemVm.getUserName());
//        bind.tvItemOrderRecommendLocation.setText( orderRecommendItemVm.getLocation());
//
//
//        Glide.with(bind.ivItemOrderRecommendUserImg).load(orderRecommendItemVm.getUserImg())
//                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                .placeholder(R.color.color_backdrop_placeholder)
//                .error(R.color.color_backdrop_placeholder)
//                .into(bind.ivItemOrderRecommendUserImg);
    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }


}
