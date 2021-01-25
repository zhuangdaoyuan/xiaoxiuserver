package com.xiuxiu.confinement_nurse.ui.news;


import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.news.vm.TokenVm;
import com.xiuxiu.confinement_nurse.ui.news.vm.UserInfoVm;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class NewsPresenter extends BasePresenter<NewsContract.IView> implements NewsContract.IPresenter {


    public NewsPresenter(NewsContract.IView viewer) {
        super(viewer);
    }

    @Override
    public void requestCustomerServiceToken() {
        RxHttp.get(Configuration.News.KEY_NEWS_TOKEN)
                .add("userId", "-999")
                .asXXResponse(TokenVm.class)
                .doOnNext(loginBean -> ModelManager.getInstance().getCacheInterface().saveRongyunToken(loginBean.getItem()))
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestCustomerServiceToken(s);
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                });
    }

    @Override
    public void requestUserToken(String id) {
        RxHttp.get(Configuration.News.KEY_NEWS_TOKEN)
                .add("userId", id)
                .asXXResponse(TokenVm.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestUserToken(s);
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                });
    }

    public void requestUserInfo(String id){
        RxHttp.get(Configuration.News.KEY_USE_INFO)
                .add("userId", id)
                .asXXResponse(UserInfoVm.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestUserInfo(s);
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                });
    }


}
