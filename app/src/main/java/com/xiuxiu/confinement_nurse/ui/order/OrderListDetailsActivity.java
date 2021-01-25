package com.xiuxiu.confinement_nurse.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityOrderListDetailsBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.model.event.OrderEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderBean;
import com.xiuxiu.confinement_nurse.utlis.BigDecimalUtils;

import java.util.List;

import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_APPLICATION_COMPLETE;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_COMPLETE;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_ORDERS;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_PAID;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_PROCESSING;

/**
 * 订单列表的详情
 */
@Route(path = RouterHelper.KEY_ORDER_LIST_DETAILS)
public class OrderListDetailsActivity extends AbsBaseActivity implements OrderListDetailsContract.IView, OrderListContract.IView {

    private String matronId;
    public static void start(Context context, String orderId) {
       start(context,orderId,"");
    }
    public static void start(Context context, String orderId,String  matronId) {
        ARouter.getInstance().build(RouterHelper.KEY_ORDER_LIST_DETAILS)
                .withString("orderId", orderId)
                .withString("matronId",matronId)
                .navigation();
    }
    private OrderBean orderBean;
    private OrderListPresenter orderListPresenter;
    private OrderListDetailsContract.IPresenter presenter;
    private ActivityOrderListDetailsBinding inflate;
    private String orderId;

    @Override
    protected View getLayoutView() {
        inflate = ActivityOrderListDetailsBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected Object getTargetView() {
        return inflate.scrollview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initViewState();
        if (TextUtils.isEmpty(orderId)) {
            finish();
            return;
        }
        setListener();
        loadData();
    }

    private void loadData() {
        presenter.requestLoadData(this.orderId);
    }

    private void setListener() {
        inflate.tvActivityOrderListDetailsViewContract.setOnClickListener(v ->
                ContractActivity.start(v.getContext(), orderId,matronId));

        //拒绝接单的操作
        inflate.vActivityOrderListDetailsReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderBean==null) {
                    return;
                }
                orderListPresenter.requestRefuse(orderBean);
            }
        });
        //同意接单的操作
        inflate.vActivityOrderListDetailsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderBean==null) {
                    return;
                }
                orderListPresenter.requestOperateOrder(orderBean);
            }
        });
    }

    private void initViewState() {
        orderListPresenter = new OrderListPresenter(this);
        presenter = new OrderListDetailsPresenter(this);
        this.orderId = getIntent().getStringExtra("orderId");
        matronId=getIntent().getStringExtra("matronId");
        presenter.setMatronId(matronId);
    }

    private void initView() {

    }

    @Override
    public void onRequestDetails(OrderBean orderBean) {
        this.orderBean=orderBean;
        //用户头像
        Glide.with(this).load(orderBean.getUserAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.color.color_backdrop_placeholder)
                .error(R.color.color_backdrop_placeholder)
                .into(inflate.activityOrderListOrderHeadPortrait);

        //服务价格

        int i = DateHelper.differentDaysByMillisecond(orderBean.getServiceEndTime(), orderBean.getServiceStartTime());
        //价格
        String servicePrice = orderBean.getServicePrice();

        String div = BigDecimalUtils.div(servicePrice, String.valueOf(100), 2);

        inflate.tvActivityOrderListPrice.setText("¥"+BigDecimalUtils.mul(div,String.valueOf(i),2));
        inflate.tvActivityOrderListTotalPrice.setText("总价:¥"+BigDecimalUtils.mul(div,String.valueOf(i),2));
//        服务类型
        inflate.tvActivityOrderListOrderName.setText(UserHelper.serviceType.get(orderBean.getServiceType()));

        //服务时间
        String s2 = DateHelper.stampToDate3(DateHelper.dateToStamp(orderBean.getServiceStartTime()));
        String s3 = DateHelper.stampToDate3(DateHelper.dateToStamp(orderBean.getServiceEndTime()));
        inflate.tvActivityOrderListTime.setText("服务时间:" + s2 + "至" + s3);

        // 用户昵称 和用户类型
        inflate.tvActivityOrderListUserName.setText(orderBean.getUserNickName() + ".个人");

        //订单信息的 用户昵称 和电话号码
        inflate.tvActivityOrderListUserName2.setText(orderBean.getContactName() + "   " + orderBean.getContactPhone());
        //订单信息里的 地址
        inflate.tvActivityOrderListUserAddress.setText(orderBean.getUserAddress());

        //服务类型中的 单/x/x胞胎
        inflate.rgActivityOrderServer1.getChildAt(0).setSelected(true);
        //服务类型中的  正常/大小三阳
        inflate.rgActivityOrderServer2.getChildAt(0).setSelected(true);


        //保险
        //隐藏掉


        if (TextUtils.equals(orderBean.getOrderStatus(), KEY_USER_ORDER_PAID)) {
            //待确认
            ViewHelper.showView(inflate.vActivityOrderListDetailsReject);
            inflate.vActivityOrderListDetailsReject.setText("拒绝接单");
            ViewHelper.showView(inflate.vActivityOrderListDetailsConfirm);
            inflate.vActivityOrderListDetailsConfirm.setText("确认接单");
        } else if (TextUtils.equals(orderBean.getOrderStatus(), KEY_USER_ORDER_ORDERS)) {
            ViewHelper.hideView(inflate.vActivityOrderListDetailsReject);
            ViewHelper.showView(inflate.vActivityOrderListDetailsConfirm);
            inflate.vActivityOrderListDetailsReject.setText("");
            inflate.vActivityOrderListDetailsConfirm.setText("上户");
        } else if (TextUtils.equals(orderBean.getOrderStatus(), KEY_USER_ORDER_PROCESSING)) {
            ViewHelper.hideView(inflate.vActivityOrderListDetailsReject);
            ViewHelper.showView(inflate.vActivityOrderListDetailsConfirm);
            inflate.vActivityOrderListDetailsConfirm.setText("申请完成");
            inflate.vActivityOrderListDetailsReject.setText("");
        } else if (TextUtils.equals(orderBean.getOrderStatus(), KEY_USER_ORDER_COMPLETE)) {
            //已经完成
            ViewHelper.hideView(inflate.vActivityOrderListDetailsReject);
            ViewHelper.hideView(inflate.vActivityOrderListDetailsConfirm);
        }else{
            ViewHelper.hideView(inflate.vActivityOrderListDetailsReject);
            ViewHelper.hideView(inflate.vActivityOrderListDetailsConfirm);
        }

        if (TextUtils.equals(orderBean.getOrderStatus(), KEY_USER_ORDER_COMPLETE)) {
            ViewHelper.showView(  inflate.tvActivityOrderListDetailsViewContract);
        }else{
            ViewHelper.hideView(  inflate.tvActivityOrderListDetailsViewContract);
        }


    }

    @Override
    public void onRequestDataList(List<OrderBean> items) {

    }

    @Override
    public void onRequestRefuseSuccess() {
        presenter.requestLoadData(this.orderId);
    }

    @Override
    public void onRequestConfirmSuccess() {
        presenter.requestLoadData(this.orderId);

        EventBus.OrderEvent().post(new OrderEvent());
    }

    @Override
    public void onRequestOrderComplete() {
        presenter.requestLoadData(this.orderId);
    }

    @Override
    public void onRequestOrderError() {

    }

    @Override
    public void onRequestRefreshing(boolean b) {

    }

    @Override
    public void onRequestNoPage() {

    }
}
