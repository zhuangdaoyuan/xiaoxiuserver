package com.xiuxiu.confinement_nurse.ui.my;


import android.graphics.drawable.GradientDrawable;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.ui.my.vm.TeamVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.UserStateInfoCodeVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.UserStateInfoVm;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class CenterPresenter extends BasePresenter<CenterContract.IView> implements CenterContract.IPresenter {

    public CenterPresenter(CenterContract.IView viewer) {
        super(viewer);
    }

    @Override
    public void requestData() {
        RxHttp.get(Configuration.User.KEY_STATE_INFO)
                .asXXResponse(UserStateInfoVm.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onResquestUserInfo(s.getItem());
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                });
    }

    @Override
    public void requestInvitationCode() {
        RxHttp.get(Configuration.User.KEY_INVITATION_CODE)
                .asXXResponse(UserStateInfoCodeVm.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onResquestUserInfoCode(s);
                    requestTeamInformation(s.getItem());
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                });
    }

    @Override
    public void requestTeamInformation(String code) {
        RxHttp.get(Configuration.User.KEY_TEAM_INFORMATION)
                .add("code", code)
                .asXXResponse(TeamVm.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestUserTeam(s);
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                });
    }

    @Override
    public void requestJoinTheTeam(String code) {
        RxHttp.postForm(Configuration.User.KEY_JOIN_CODE)
                .add("code", code)
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("加入成功");
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                });
    }
}
