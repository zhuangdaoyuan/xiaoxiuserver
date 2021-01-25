package com.xiuxiu.confinement_nurse.ui.order;


import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderBean;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderListItemVm;
import com.xiuxiu.confinement_nurse.ui.order.vm.PageBean;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttpNoBodyParam;

import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_APPLICATION_COMPLETE;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_ORDERS;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_PAID;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_USER_ORDER_PROCESSING;

public class OrderListPresenter extends BasePresenter<OrderListContract.IView> implements OrderListContract.IPresenter {
    private int pageNo = 1;
    private String type;
    private PageBean pageBean;
    private boolean isJiGoud;
    private String matronId;
    public OrderListPresenter(OrderListContract.IView viewer) {
        super(viewer);
    }

    public void setJiGoud(boolean jiGoud) {
        isJiGoud = jiGoud;
    }

    @Override
    public void requestOrderData(String type) {
        this.type = type;
        pageNo = 1;
        getViewer().onRequestRefreshing(true);


        requestData(pageNo, new XFunc1<List<OrderBean>>() {
            @Override
            public void call(List<OrderBean> items) {
                if (items == null || items.isEmpty()) {
                    getViewer().onRequestPageEmpty();
                } else {
                    getViewer().onRequestPageSuccess();
                    getViewer().onRequestDataList(items);
                }
                getViewer().onRequestRefreshing(false);
            }
        }, new XFunc0() {
            @Override
            public void call() {
                getViewer().onRequestPageError(0);
                getViewer().onRequestRefreshing(false);
            }
        });
    }

    @Override
    public void requestLoadMore() {
        if (pageNo >= pageBean.getPageSize()) {
            getViewer().onRequestOrderComplete();
            return;
        }
        int index = pageNo + 1;
        requestData(index, new XFunc1<List<OrderBean>>() {
            @Override
            public void call(List<OrderBean> items) {
                pageNo++;
                if (items != null && !items.isEmpty()) {
                    getViewer().onRequestDataList(items);
                } else {
                    getViewer().onRequestNoPage();
                }
            }
        }, new XFunc0() {
            @Override
            public void call() {
                getViewer().onRequestOrderError();
            }
        });
    }


    private void requestData(int pageNo, XFunc1<List<OrderBean>> mSuccessCallBack, XFunc0 mErrorCallBack) {
        requestData(pageNo)
                .doOnNext(new Consumer<OrderListItemVm>() {
                    @Override
                    public void accept(OrderListItemVm orderListItemVm) throws Exception {
                        pageBean = orderListItemVm.getPageBean();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(new Consumer<OrderListItemVm>() {
                    @Override
                    public void accept(OrderListItemVm orderListItemVm) throws Exception {
                        mSuccessCallBack.call(orderListItemVm.getItems());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        }
                        if (mErrorCallBack != null) {
                            mErrorCallBack.call();
                        }
                    }
                });
    }

    private Observable<OrderListItemVm> requestData(int pageNo) {
        if (TextUtils.isEmpty(matronId)) {
            RxHttpNoBodyParam add = RxHttp.get(Configuration.Order.KEY_ORDER_LIST)
                    .add("orderStatus", type)
                    .add("pageNo", pageNo)
                    .add("pageSize", 30);
            return add
                    .asXXResponse(OrderListItemVm.class);
        }else{
            if (isJiGoud) {
                return  RxHttp.postForm(Configuration.mechanism.KEY_ORDER_LIST_All)
                        .add("orderStatus", type)
                        .add("pageNo", pageNo)
                        .add("ysId",matronId)
                        .add("pageSize", 30)
                        .asXXResponse(OrderListItemVm.class);
            }
            return  RxHttp.postForm(Configuration.mechanism.KEY_ORDER_LIST)
                    .add("orderStatus", type)
                    .add("pageNo", pageNo)
                    .add("ysId",matronId)
                    .add("pageSize", 30)
                    .asXXResponse(OrderListItemVm.class);
        }
    }



    @Override
    public void requestRefuse(OrderBean orderBean) {
        if (TextUtils.equals(orderBean.getOrderStatus(), KEY_USER_ORDER_PAID)) {
            requestConfirm(orderBean.getOrderId(), false);
        }
    }


    @Override
    public void requestOperateOrder(OrderBean orderRecommendItemVm) {
        if (TextUtils.equals(orderRecommendItemVm.getOrderStatus(), KEY_USER_ORDER_PAID)) {
            requestConfirm(orderRecommendItemVm.getOrderId(), true);
        } else if (TextUtils.equals(orderRecommendItemVm.getOrderStatus(), KEY_USER_ORDER_ORDERS)) {
            requestOnTheDoor(orderRecommendItemVm.getOrderId(), true);
        } else if (TextUtils.equals(orderRecommendItemVm.getOrderStatus(), KEY_USER_ORDER_PROCESSING)) {
            requestApplyFinish(orderRecommendItemVm.getOrderId());
        }
    }


    @Override
    public void requestYsId(String matronId) {
        this.matronId = matronId;
    }

    private void requestApplyFinish(String orderId) {
        if (TextUtils.isEmpty(matronId)) {
            RxHttp.postForm(Configuration.Order.KEY_ORDER_APPLY_FINISH)
                    .add("orderId", orderId)
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        getViewer().onRequestConfirmSuccess();
                        ToastHelper.showToast("操作成功");
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        } else {
                            ToastHelper.showToast("请稍后重试");
                        }
                    });
        }else{
            RxHttp.postForm(Configuration.mechanism.KEY_ORDER_APPLY_FINISH)
                    .add("orderId", orderId)
                    .add("ysId",matronId)
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        getViewer().onRequestConfirmSuccess();
                        ToastHelper.showToast("操作成功");
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        } else {
                            ToastHelper.showToast("请稍后重试");
                        }
                    });
        }

    }

    private void requestOnTheDoor(String orderId, boolean ensure) {
        if (TextUtils.isEmpty(matronId)) {
            RxHttp.postForm(Configuration.Order.KEY_ORDER_ON_THE_DOOR)
                    .add("orderId", orderId)
                    .add("onTheDoor", ensure)
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        if (ensure) {
                            getViewer().onRequestConfirmSuccess();
                        }
                        ToastHelper.showToast("操作成功");
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        } else {
                            ToastHelper.showToast("请稍后重试");
                        }
                    });
        }else{
            RxHttp.postForm(Configuration.mechanism.KEY_ORDER_ON_THE_DOOR)
                    .add("orderId", orderId)
                    .add("ysId",matronId)
                    .add("onTheDoor", ensure)
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        if (ensure) {
                            getViewer().onRequestConfirmSuccess();
                        }
                        ToastHelper.showToast("操作成功");
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        } else {
                            ToastHelper.showToast("请稍后重试");
                        }
                    });
        }

    }

    private void requestConfirm(String orderId, boolean ensure) {
        if (TextUtils.isEmpty(matronId)) {
            RxHttp.postForm(Configuration.Order.KEY_ORDER_CONFIRM)
                    .add("orderId", orderId)
                    .add("ensure", ensure)
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        if (ensure) {
                            getViewer().onRequestConfirmSuccess();
                        } else {
                            getViewer().onRequestRefuseSuccess();
                        }
                        ToastHelper.showToast("操作成功");
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        } else {
                            ToastHelper.showToast("请稍后重试");
                        }
                    });
        }else{
            RxHttp.postForm(Configuration.mechanism.KEY_ORDER_CONFIRM)
                    .add("orderId", orderId)
                    .add("ensure", ensure)
                    .add("ysId",matronId)
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        if (ensure) {
                            getViewer().onRequestConfirmSuccess();
                        } else {
                            getViewer().onRequestRefuseSuccess();
                        }
                        ToastHelper.showToast("操作成功");
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        } else {
                            ToastHelper.showToast("请稍后重试");
                        }
                    });
        }
        
    }
}
