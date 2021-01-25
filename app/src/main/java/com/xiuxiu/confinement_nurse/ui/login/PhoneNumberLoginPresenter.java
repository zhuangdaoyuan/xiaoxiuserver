package com.xiuxiu.confinement_nurse.ui.login;


import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UtilsHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.utlis.Utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class PhoneNumberLoginPresenter extends BasePresenter<PhoneNumberLoginContract.IView> implements PhoneNumberLoginContract.IPresenter {
    PhoneNumberUtil phoneNumberUtil;

    final String countryCode = Locale.getDefault().getCountry();

    private String mPostPhone;

    public PhoneNumberLoginPresenter(PhoneNumberLoginContract.IView viewer) {
        super(viewer);
        phoneNumberUtil = PhoneNumberUtil.createInstance(Utils.getApp());
    }


    /**
     * 发送验证码
     */
    @Override
    public void requestPostCode(String phone) {
        requestPostCode(phone, ModelManager.getInstance().getCacheInterface().getUserType());
    }

    @Override
    public void requestPostCode(String phone, int userType) {
        if (TextUtils.isEmpty(phone)) {
            ToastHelper.showToast("手机号码为空");
            return;
        }
        Phonenumber.PhoneNumber number = null;
        try {
            number = phoneNumberUtil.parse(phone, countryCode);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        boolean isValid = phoneNumberUtil.isValidNumber(number);
        if (!isValid) {
            ToastHelper.showToast("手机号码验证失败");
            return;
        }
        Observable.just("")
                .throttleFirst(1, TimeUnit.SECONDS)
                .flatMap((Function<String, ObservableSource<String>>) s ->
                        RxHttp.postForm(Configuration.KEY_REGISTER_CODE)
                                .add("userType",String.valueOf(userType))
                                .add("phone", phone)
                                .asXXResponse(String.class))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("发送成功");
                    countdown();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }else{
                        ToastHelper.showToast("发送失败");
                    }
                });
    }


    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param code
     */
    @Override
    public void requestRegister(String phone, String password, String code) {

        if (isValid(phone, password)) return;
        RxHttp.postForm(Configuration.KEY_REGISTER)
                .add("userType",  ModelManager.getInstance().getCacheInterface().getUserType())
                .add("phone", phone)
                .add("password", password).add("code", code)
                .asXXResponse(LoginBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("注册成功");
                    getViewer().onRequestRegisterSuccess();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }else{
                        ToastHelper.showToast("注册失败");
                    }
                });
    }

    @Override
    public void requestChangePhone(String phone, String code) {
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phone, countryCode);
            boolean isValid = phoneNumberUtil.isValidNumber(number);
            if (!isValid) {
                ToastHelper.showToast("手机号码验证失败");
                return ;
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
            return ;
        }
        if (TextUtils.isEmpty(code)) {
            ToastHelper.showToast("验证码失败");
            return;
        }
        RxHttp.postForm(Configuration.KEY_PHONE)
                .add("userType", ModelManager.getInstance().getCacheInterface().getUserType())
                .add("phone", phone)
                .add("code", code)
                .asXXResponse(LoginBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("更换成功");
                    getViewer().onRequestChangeSuccess();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }else{
                        ToastHelper.showToast("失败");
                    }

                });
    }

    @Override
    public void requestResetPasswordByPassword(String phone, String code, String newPassword, String password) {
        if (TextUtils.isEmpty(newPassword)||TextUtils.isEmpty(password)) {
            ToastHelper.showToast("密码不能为空");
            return;
        }
        if (!TextUtils.equals(newPassword,password)) {
            ToastHelper.showToast("2次密码不一致");
            return;
        }

        RxHttp.postForm(Configuration.KEY_RESET_PASSWORD_BY_PHONE)
                .add("userType",  ModelManager.getInstance().getCacheInterface().getUserType())
                .add("phone", phone)
                .add("code", code)
                .add("password", password)
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("修改成功");
                    getViewer().onRequestChangeSuccess();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                        return;
                    }
                    ToastHelper.showToast("修改失败");
                });
    }


    /**
     * 重置密码 发送的验证码
     *
     * @param phone
     */
    @Override
    public void requestResetPasswordCode(String phone) {
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
        RxHttp.postForm(Configuration.KEY_RESET_PASSWORD_CODE)
                .add("userType", ModelManager.getInstance().getCacheInterface().getUserType())
                .add("phone", phone)
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    mPostPhone = phone;
                    ToastHelper.showToast("发送成功");
                    countdown();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }else{
                        ToastHelper.showToast("发送失败");
                    }
                });
    }


    /**
     * 重置密码 校验验证吗
     *
     * @param phone
     */
    @Override
    public void requestResetPasswordCheck(String phone, String code) {
        if (!TextUtils.equals(mPostPhone, phone)) {
            ToastHelper.showToast("手机号码不正确，请重新发送验证码");
            return;
        }
        RxHttp.postForm(Configuration.KEY_RESET_PASSWORD_CHECK)
                .add("userType", ModelManager.getInstance().getCacheInterface().getUserType())
                .add("phone", phone)
                .add("code", code)
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestNext(phone,code);
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }else{
                        ToastHelper.showToast("验证失败，请重新发送验证码");
                    }
                });
    }


    @Override
    public void requestResetPasswordByPassword(String oldPassword, String newPassword) {

        if (isValidPassword(oldPassword)) return;

        if (isValidPassword(newPassword)) return;


        RxHttp.postForm(Configuration.KEY_RESET_PASSWORD_BY_PASSWORD)
                .add("oldPwd", oldPassword)
                .add("pwd", newPassword)
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestResetPasswordSuccess();
                    ToastHelper.showToast("密码设置成功");
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                        return;
                    }
                    ToastHelper.showToast("密码设置失败");
                });
    }

    private boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            ToastHelper.showToast("密码为空");
            return true;
        }
        if (password.length() < 6) {
            ToastHelper.showToast("密码错误");
            return true;
        }
        if (!UtilsHelper.isPassword2(password)) {
            ToastHelper.showToast("密码错误");
            return true;
        }
        return false;
    }


    private int NUM = 60;
    private Disposable disposable;

    private void countdown() {
        if (disposable != null) {
            disposable.dispose();
        }
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(NUM)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        getViewer().onRequestPostCodeNum(NUM - aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        getViewer().onRequestPostCodeNumComplete();
                    }
                });

    }

    private boolean isValid(String phone, String password) {
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phone, countryCode);
            boolean isValid = phoneNumberUtil.isValidNumber(number);
            if (!isValid) {
                ToastHelper.showToast("手机号码验证失败");
                return true;
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
            return true;
        }
        if (TextUtils.isEmpty(password)) {
            ToastHelper.showToast("密码为空");
            return true;
        }
        if (password.length() < 6) {
            ToastHelper.showToast("密码错误");
            return true;
        }
        if (!UtilsHelper.isPassword2(password)) {
            ToastHelper.showToast("密码错误");
            return true;
        }
        return false;
    }


}
