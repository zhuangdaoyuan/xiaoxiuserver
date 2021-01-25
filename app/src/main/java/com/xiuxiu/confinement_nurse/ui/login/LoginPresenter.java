package com.xiuxiu.confinement_nurse.ui.login;


import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.UtilsHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.cache.CacheInterface;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.http.bean.Response;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.utlis.Utils;


import java.util.Locale;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class LoginPresenter extends BasePresenter<LoginContract.IView> implements LoginContract.IPresenter {


    final String countryCode = Locale.getDefault().getCountry();
    CacheInterface cacheAccessor;
    PhoneNumberUtil phoneNumberUtil;

    public LoginPresenter(LoginContract.IView viewer) {
        super(viewer);
        cacheAccessor = ModelManager.getInstance().getCacheInterface();
        phoneNumberUtil = PhoneNumberUtil.createInstance(Utils.getApp());
    }

    /**
     * 登录
     *
     * @param phone
     * @param password
     */
    @Override
    public void requestLogin(String phone, String password,boolean isYuesao) {
        if (TextUtils.isEmpty(phone)) {
            ToastHelper.showToast("手机号码不能为空");
            return;
        }
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phone, countryCode);
            boolean isValid = phoneNumberUtil.isValidNumber(number);
            if (!isValid) {
                ToastHelper.showToast("手机号码验证失败");
                return;
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastHelper.showToast("密码为空");
            return;
        }
        if (password.length() < 6) {
            ToastHelper.showToast("密码错误");
            return;
        }
        if (!UtilsHelper.isPassword2(password)) {
            ToastHelper.showToast("密码错误");
            return;
        }
        if (isYuesao) {
            RxHttp.postForm(Configuration.KEY_LOGIN)
                    .add("userType", "2")
                    .add("phone", phone)
                    .add("password", password)
                    .asXXResponse(LoginBean.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        ToastHelper.showToast("登录成功");
                        ModelManager.getInstance().getCacheInterface().saveUserPhone(phone);
                        getViewer().onRequestLoginSuccess(s.getXtoken(), s.getUserId());
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        } else {
                            ToastHelper.showToast("登录失败");
                        }
                    });
        }else{
            RxHttp.postForm(Configuration.KEY_LOGIN_MECHANISM)
                    .add("userType", "4")
                    .add("phone", phone)
                    .add("password", password)
                    .asXXResponse(LoginBean.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        ToastHelper.showToast("登录成功");
                        ModelManager.getInstance().getCacheInterface().saveUserPhone(phone);
                        getViewer().onRequestAgencyLoginSuccess(s.getXtoken(), s.getUserId());
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            String message = throwable.getMessage();
                            if (TextUtils.equals(message,"该机构审核未通过。请重新提交审核")) {
                                getViewer().onRequestAginVerify();
                            }
                            ToastHelper.showToast(message);
                        } else {
                            ToastHelper.showToast("登录失败");
                        }
                    });
        }
    }

    /**
     * 登出
     */
    @Override
    public void requestLogout() {
        RxHttp.postForm(Configuration.KEY_LOGOUT)
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                }, throwable -> {
                });
    }


    /**
     * -01 等待审核
     *         -02 审核不通过
     *         -03 重新提交审核
     *         -04 审核通过
     * @param state
     * @return
     */
    private Observable<String> requestInstitutionalReviewState(String userId,int state){
        return RxHttp.postForm(Configuration.KEY_MECHANISM_STATE)
                .add("id", userId)
//                .add("ty", state)
                .asXXResponse(String.class);
    }

}
