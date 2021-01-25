package com.xiuxiu.confinement_nurse.ui.office;


import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeDetailsVm;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class OfficeDetailsPresenter extends BasePresenter<OfficeDetailsContract.IView> implements OfficeDetailsContract.IPresenter {

    public OfficeDetailsPresenter(OfficeDetailsContract.IView viewer) {
        super(viewer);
    }

    @Override
    public void requestOrderDetails(String orederId) {
        RxHttp.get(Configuration.Office.KEY_POST_DETAIL)
                .add("employmentId", orederId)
                .asXXResponse(OfficeDetailsVm.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestPageSuccess();
                    getViewer().onRequestOrderDetails(s.getItem());
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                    getViewer().onRequestPageError(0);
                });
    }
    ///////////////投递简历////////////////////////////////////
    @Override
    public void requestDelivery( String employmentId) {
        RxHttp.postForm(Configuration.Office.KEY_POST_RESUME)
                .add("employmentId", employmentId)
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("投递成功");
                    getViewer().onRequestDelivery();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    } else {
                        ToastHelper.showToast("投递失败");
                    }
                });
    }
}
