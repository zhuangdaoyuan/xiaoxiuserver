package com.xiuxiu.confinement_nurse.ui.user;


import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.Constants;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.IntCodeEnum;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.AgencyInfoBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.CertificateInfoBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.ui.news.vm.UserInfoVm;
import com.xiuxiu.confinement_nurse.ui.user.vm.AgencyInfoBeanVm;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttpFormParam;

public class UserInfoPresenter extends BasePresenter<Viewer> implements UserInfoContract.IPresenter {
    private String ysId;
    public UserInfoPresenter(Viewer viewer) {
        super(viewer);
    }


    public String getYsId() {
        return ysId;
    }

    public void setYsId(String ysId) {
        this.ysId = ysId;
    }

    /**
     * 获取用户信息
     *
     * @param xtoken
     * @param userId
     * @param mSuccessCallBack
     * @param mErrorCallBack
     */
    @Override
    public void requestUserInfo(String xtoken, String userId, XFunc0 mSuccessCallBack, XFunc0 mErrorCallBack) {
        RxHttp.get(Configuration.User.KEY_USER_INFO)
                .addHeader("userType",Constants.KEY_USE)
                .addHeader("xtoken", xtoken)
                .asXXResponse(UserBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    s.setId(userId);
                    s.setToken(xtoken);
                    s.setUserType(Constants.KEY_USE);
                    UserHelper.switchUser(s);
                    if (mSuccessCallBack != null) {
                        mSuccessCallBack.call();
                    }

                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                    if (mErrorCallBack != null) {
                        mErrorCallBack.call();
                    }
                });
    }
    @Override
    public void requestUserInfoByCertificate(String ysId,XFunc1<Boolean> mSuccessCallBack, XFunc0 mErrorCallBack){
        Observable<CertificateInfoBean> ysId1;
        if (TextUtils.isEmpty(ysId)) {
            ysId1= RxHttp.get(Configuration.User.KEY_CERTIFICATE_INFO)
                    .asXXResponse(CertificateInfoBean.class);
        }else{
           ysId1 = RxHttp.postForm(Configuration.mechanism.KEY_CERTIFICATE_INFO)
                    .add("ysId", ysId)
                    .asXXResponse(CertificateInfoBean.class);
        }
        ysId1
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
//                    UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
//                    userBean.setCertificateInfoBean(s);

                    if (mSuccessCallBack == null) {
                        return;
                    }
                    boolean is1 = false;
                    boolean is2 = false;
                    boolean is3 = false;
                    boolean is4 = false;
                    boolean is5 = false;
                    boolean is6 = false;

                    for (CertificateInfoBean.CertificateInfo c : s.getItems()) {
                        if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_FACE_ID)) {
                            if (TextUtils.equals(c.getState(), String.valueOf(UserHelper.KEY_CERTIFIED))) {
                                is2 = true;
                            }
                        } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_ID_CARD)) {
                            if (TextUtils.equals(c.getState(), String.valueOf(UserHelper.KEY_CERTIFIED))) {
                                is1 = true;
                            }
                        } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_HEALTH_CERT)) {
                            if (TextUtils.equals(c.getState(), String.valueOf(UserHelper.KEY_CERTIFIED))) {
                                is3 = true;
                            }
                        } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_NO_CRIMINAL_CERT)) {
                            if (TextUtils.equals(c.getState(), String.valueOf(UserHelper.KEY_CERTIFIED))) {
                                is4 = true;
                            }
                        } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_MATRON_CERT)) {
                            if (TextUtils.equals(c.getState(), String.valueOf(UserHelper.KEY_CERTIFIED))) {
                                is5 = true;
                            }
                        } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_NURSEMAID_CERT)) {
                            if (TextUtils.equals(c.getState(), String.valueOf(UserHelper.KEY_CERTIFIED))) {
                                is6 = true;
                            }
                        }
                    }
                    if (is5 || is6) {
                        mSuccessCallBack.call(true);
                    } else {
                        mSuccessCallBack.call(false);
                    }
                }, throwable -> {
                    if (mErrorCallBack != null) {
                        mErrorCallBack.call();
                    }
                });
    }
    /**
     * 获取认证信息
     *
     * @param mSuccessCallBack
     * @param mErrorCallBack
     */
    @Override
    public void requestUserInfoByCertificate(XFunc1<Boolean> mSuccessCallBack, XFunc0 mErrorCallBack) {
        requestUserInfoByCertificate("",mSuccessCallBack,mErrorCallBack);

    }
    public void requestAgencyInfo( XFunc0 mSuccessCallBack, XFunc0 mErrorCallBack){
        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
        requestAgencyInfo(userBean.getToken(),userBean.getId(),mSuccessCallBack,mErrorCallBack);
    }

    @Override
    public void requestAgencyInfo(String xtoken, String id, XFunc0 mSuccessCallBack, XFunc0 mErrorCallBack) {
        RxHttp.postForm(Configuration.mechanism.KEY_JG_INFO)
                .add("userType", Constants.KEY_AGENCY)
                .add("id", id)
                .addHeader("xtoken", xtoken)
                .asXXResponse(AgencyInfoBeanVm.class)
                .map(agencyInfoBean1 -> {
                    AgencyInfoBean agencyInfoBean=agencyInfoBean1.getData();
                    UserBean userBean = new UserBean(agencyInfoBean.getId());
                    userBean.setToken(xtoken);
                    userBean.setId(id);
                    userBean.setUserType(Constants.KEY_AGENCY);
                    userBean.setName(agencyInfoBean.getTitle());
                    userBean.setAgencyInfoBean(agencyInfoBean);
                    return userBean;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    UserHelper.switchUser(s);
                    if (mSuccessCallBack != null) {
                        mSuccessCallBack.call();
                    }

                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                    if (mErrorCallBack != null) {
                        mErrorCallBack.call();
                    }
                });
    }
}
