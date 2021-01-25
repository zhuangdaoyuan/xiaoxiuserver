package com.xiuxiu.confinement_nurse.ui.order.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutOrderlistBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderBean;
import com.xiuxiu.confinement_nurse.utlis.BigDecimalUtils;

import java.math.BigDecimal;

import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_APPLICATION_COMPLETE;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_COMPLETE;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_ORDERS;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_PAID;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_PROCESSING;

public class OrderListView extends LinearLayout {

    private LayoutOrderlistBinding bind;

    public OrderListView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public OrderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public OrderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_orderlist, this);
        bind = LayoutOrderlistBinding.bind(inflate);
        initView();
        initViewState();
        setListener();

    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    public void loadData(OrderBean orderRecommendItemVm) {
        //服务类型
        bind.tvItemOrderRecommendTitle.setText(UserHelper.serviceType.get(orderRecommendItemVm.getServiceType()));


        String serviceStartTime = orderRecommendItemVm.getServiceStartTime();
        String serviceEndTime = orderRecommendItemVm.getServiceEndTime();

        int i = DateHelper.differentDaysByMillisecond(orderRecommendItemVm.getServiceEndTime(), orderRecommendItemVm.getServiceStartTime());
        //价格
        String servicePrice = orderRecommendItemVm.getServicePrice();

        String div = BigDecimalUtils.div(servicePrice, String.valueOf(100), 2);

        bind.tvItemOrderRecommendPrice.setText("¥"+BigDecimalUtils.mul(div,String.valueOf(i),2));
        //服务时间
//        String s2 = DateHelper.stampToDate3(DateHelper.dateToStamp(orderRecommendItemVm.getServiceStartTime()));
//        String s3 = DateHelper.stampToDate3(DateHelper.dateToStamp(orderRecommendItemVm.getServiceEndTime()));
        bind.tvItemOrderRecommendTime.setText("服务时间:" + serviceStartTime + "至" + serviceEndTime+"("+i+")天");

        //用户头像
         Glide.with(bind.ivItemOrderRecommendUserImg).load(orderRecommendItemVm.getUserAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.color.color_backdrop_placeholder)
                .error(R.color.color_backdrop_placeholder)
                .into(bind.ivItemOrderRecommendUserImg);
        //用户名称
        bind.tvItemOrderRecommendUserName.setText(orderRecommendItemVm.getUserNickName()+".个人");
        //用户地址
        bind.tvItemOrderRecommendLocation.setText(orderRecommendItemVm.getUserLocationName());
        //订单状态 是否加急
        bind.tvItemOrderRecommendState.setText("");

        //订单状态

        if (TextUtils.equals(orderRecommendItemVm.getOrderStatus(),KEY_USER_ORDER_PAID)) {
            //待确认
            ViewHelper.showView(bind.tvLayoutOrderlistReject);
            bind.tvLayoutOrderlistConfirm.setText("确认接单");
            bind.tvLayoutOrderlistConfirm.setEnabled(true);
        }else if (TextUtils.equals(orderRecommendItemVm.getOrderStatus(),KEY_USER_ORDER_ORDERS)) {
            //待进行中
            bind.tvLayoutOrderlistConfirm.setText("上户");
            bind.tvLayoutOrderlistConfirm.setEnabled(true);
            ViewHelper.hideView(bind.tvLayoutOrderlistReject);
            ViewHelper.showView(bind.tvLayoutOrderlistConfirm);
        }else  if (TextUtils.equals(orderRecommendItemVm.getOrderStatus(),KEY_USER_ORDER_PROCESSING)) {
            //已经完成
            bind.tvLayoutOrderlistConfirm.setText("申请完成");
            bind.tvLayoutOrderlistConfirm.setEnabled(true);
            ViewHelper.hideView(bind.tvLayoutOrderlistReject);
            ViewHelper.showView(bind.tvLayoutOrderlistConfirm);;
        }else  if (TextUtils.equals(orderRecommendItemVm.getOrderStatus(),KEY_USER_ORDER_COMPLETE)) {
            //已经完成
            ViewHelper.hideView(bind.tvLayoutOrderlistReject);
            ViewHelper.hideView(bind.tvLayoutOrderlistConfirm);
        }else{
            ViewHelper.hideView(bind.tvLayoutOrderlistReject);
            ViewHelper.hideView(bind.tvLayoutOrderlistConfirm);
        }
    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }



}
