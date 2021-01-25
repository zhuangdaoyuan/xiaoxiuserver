package com.xiuxiu.confinement_nurse.ui.order;


import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderBean;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderDetailsVm;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class OrderListDetailsPresenter extends BasePresenter<OrderListDetailsContract.IView> implements OrderListDetailsContract.IPresenter {
    private String matronId;

    public OrderListDetailsPresenter(OrderListDetailsContract.IView viewer) {
        super(viewer);
    }
    @Override
    public void setMatronId(String matronId) {
        this.matronId = matronId;
    }

    @Override
    public void requestLoadData(String orderid) {
        getViewer().onRequestLoading();

        if (TextUtils.isEmpty(matronId)) {
            RxHttp.get(Configuration.Order.KEY_ORDER_DETAIL)
                    .add("orderId", orderid)
                    .asXXResponse(OrderDetailsVm.class)
                    .map(new Function<OrderDetailsVm, OrderBean>() {
                        @Override
                        public OrderBean apply(OrderDetailsVm orderDetailsVm) throws Exception {
                            return orderDetailsVm.getItem();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        getViewer().onRequestDetails(s);
                        getViewer().onRequestPageSuccess();
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        }
                        getViewer().onRequestPageError(0);
                    });
        }else{
            RxHttp.get(Configuration.mechanism.KEY_ORDER_DETAIL)
                    .add("orderId", orderid)
                    .add("ysId",matronId)
                    .asXXResponse(OrderDetailsVm.class)
                    .map(new Function<OrderDetailsVm, OrderBean>() {
                        @Override
                        public OrderBean apply(OrderDetailsVm orderDetailsVm) throws Exception {
                            return orderDetailsVm.getItem();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        getViewer().onRequestDetails(s);
                        getViewer().onRequestPageSuccess();
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        }
                        getViewer().onRequestPageError(0);
                    });
        }



    }
}
