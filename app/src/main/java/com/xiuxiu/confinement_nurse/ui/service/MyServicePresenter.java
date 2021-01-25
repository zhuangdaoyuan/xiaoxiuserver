package com.xiuxiu.confinement_nurse.ui.service;


import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.event.ServiceEvent;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.service.vm.MyServerInfoVm;
import com.xiuxiu.confinement_nurse.ui.service.vm.MyServiceVm;
import com.xiuxiu.confinement_nurse.utlis.BigDecimalUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttpFormParam;

public class MyServicePresenter extends BasePresenter<MyServiceContract.IView> implements MyServiceContract.IPresenter {

    private String  ysId;

    public void setYsId(String ysId) {
        this.ysId = ysId;
    }

    public MyServicePresenter(MyServiceContract.IView viewer) {
        super(viewer);
    }

    @Override
    public void requestMyServiceListData() {
        if (TextUtils.isEmpty(ysId)) {
            RxHttp.get(Configuration.User.KEY_SERVICE_LiST_GET)
                    .asXXResponse(MyServiceVm.class)
                    .map(new Function<MyServiceVm, List<MyServerInfoVm>>() {
                        @Override
                        public List<MyServerInfoVm> apply(MyServiceVm myServiceVm) throws Exception {
                            return myServiceVm.getItems();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        if (s.isEmpty()) {
                            getViewer().onRequestPageEmpty();
                        } else {
                            getViewer().onRequestMyServiceList(s);
                            getViewer().onRequestPageSuccess();
                        }
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        }
                        getViewer().onRequestPageError(0);
                    });
        }else{
            RxHttp.postForm(Configuration.mechanism.KEY_SERVICE_LiST_GET)
                    .add("ysId",ysId)
                    .asXXResponse(MyServiceVm.class)
                    .map(new Function<MyServiceVm, List<MyServerInfoVm>>() {
                        @Override
                        public List<MyServerInfoVm> apply(MyServiceVm myServiceVm) throws Exception {
                            return myServiceVm.getItems();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        if (s.isEmpty()) {
                            getViewer().onRequestPageEmpty();
                        } else {
                            getViewer().onRequestMyServiceList(s);
                            getViewer().onRequestPageSuccess();
                        }
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        }
                        getViewer().onRequestPageError(0);
                    });
        }

    }

    @Override
    public void requestSubmitService(MyServerInfoVm myServerInfoVm) {
        if (TextUtils.isEmpty(myServerInfoVm.getTotalPrice())) {
            ToastHelper.showToast("金额不能为空");
            return;
        }
        String id = myServerInfoVm.getId();
        RxHttpFormParam rxHttpFormParam;
        if (TextUtils.isEmpty(ysId)) {
            if (!TextUtils.isEmpty(id)) {
                rxHttpFormParam = RxHttp.postForm(Configuration.User.KEY_SERVICE_LIST_UPDATE)
                        .add("id", id);
            } else {
                rxHttpFormParam = RxHttp.postForm(Configuration.User.KEY_SERVICE_LIST_ADD);
            }
        }else{
            if (!TextUtils.isEmpty(id)) {
                rxHttpFormParam = RxHttp.postForm(Configuration.mechanism.KEY_SERVICE_LIST_UPDATE)
                        .add("id", id).add("ysId",ysId);
            } else {
                rxHttpFormParam = RxHttp.postForm(Configuration.mechanism.KEY_SERVICE_LIST_ADD)
                        .add("ysId",ysId);
            }
        }
        rxHttpFormParam
                .add("serviceType", myServerInfoVm.getServiceType())
                .add("specialCare", myServerInfoVm.getSpecialCare())
                .add("otherSkills", myServerInfoVm.getOtherSkills())
                .add("days", myServerInfoVm.getDays())
                .add("totalPrice", BigDecimalUtils.mul(myServerInfoVm.getTotalPrice(),String.valueOf(100),0))
                .add("timeType", myServerInfoVm.getTimeType())
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    EventBus.AddService().post(new ServiceEvent());
                    ToastHelper.showToast("提交成功");
                    getViewer().onRequestFinish();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    } else {
                        ToastHelper.showToast("提交失败");
                    }
                });
    }

    @Override
    public void requestDelete(int position, MyServerInfoVm safe) {
        if (TextUtils.isEmpty(ysId)) {
            RxHttp.postForm(Configuration.User.KEY_SERVICE_LIST_DELETE)
                    .add("id", safe.getId())
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        ToastHelper.showToast("删除成功");
                        getViewer().onRequestDeleteSuccess(position);
                    }, throwable -> {
                        ToastHelper.showToast("删除失败");
                    });
        }else{
            RxHttp.postForm(Configuration.mechanism.KEY_SERVICE_LIST_DELETE)
                    .add("id", safe.getId())
                    .add("ysId",ysId)
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        ToastHelper.showToast("删除成功");
                        getViewer().onRequestDeleteSuccess(position);
                    }, throwable -> {
                        ToastHelper.showToast("删除失败");
                    });
        }
    }

    @Override
    public void requestEditData(MyServerInfoVm myServerInfoVm) {

    }


}
