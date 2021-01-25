package com.xiuxiu.confinement_nurse.ui.login;

import android.net.Uri;
import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.AgencyInfoBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class AgencyRegisterPresenter extends BasePresenter<AgencyRegisterContract.IView> implements AgencyRegisterContract.IPresenter {
    private String mCurrentSelectImage1;
    private String mCurrentSelectImage2;

    public AgencyRegisterPresenter(AgencyRegisterContract.IView viewer) {
        super(viewer);
    }

    @Override
    public void requestUploadFile(List<String> mPaths, List<Uri> uris) {
        mCurrentSelectImage1 = CollectionUtil.getSafe(mPaths, 0, "");
        mCurrentSelectImage2 = CollectionUtil.getSafe(mPaths, 1, "");
    }

    @Override
    public void requestSub(String phone, String code, String password, String name, String person, String title1, String s) {
        if (TextUtils.isEmpty(phone)) {
            ToastHelper.showToast("电话号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastHelper.showToast("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastHelper.showToast("机构名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(person)) {
            ToastHelper.showToast("机构联系人不能为空");
            return;
        }
        if (TextUtils.isEmpty(title1)) {
            ToastHelper.showToast("机构联系电话不能为空");
            return;
        }
        if (TextUtils.isEmpty(mCurrentSelectImage1)) {
            ToastHelper.showToast("请选择机构证件");
            return;
        }
        if (TextUtils.isEmpty(mCurrentSelectImage2)) {
            ToastHelper.showToast("请选择机构证件");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastHelper.showToast("密码不能为空");
            return;
        }
        //上传图片
        Observable<String> uploadUrl = createUploadUrl(mCurrentSelectImage1);
        Observable<String> uploadUrl1 = createUploadUrl(mCurrentSelectImage2);
        Observable
                .zip(uploadUrl, uploadUrl1, new BiFunction<String, String, List<String>>() {
                    @NonNull
                    @Override
                    public List<String> apply(@NonNull String s, @NonNull String s2) throws Exception {
                        List<String> mUris = new ArrayList<>();
                        mUris.add(s);
                        mUris.add(s2);
                        return mUris;
                    }
                })
                .flatMap(new Function<List<String>, ObservableSource<LoginBean>>() {
                    @Override
                    public ObservableSource<LoginBean> apply(@NonNull List<String> strings) throws Exception {
                        return RxHttp.postForm(Configuration.mechanism.KEY_JG_REGISTER)
                                .add("title", name)
                                .add("orgMobile", title1)
                                .add("contact", person)
                                .add("foreImg", strings.get(0))
                                .add("tailImg", strings.get(1))
                                .add("phone", phone)
                                .add("code", code)
                                .add("des", s)
                                .add("password", password)
                                .add("userType", "4")
                                .asXXResponse(LoginBean.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean loginBean) throws Exception {
                        ToastHelper.showToast("提交成功");
                        getViewer().onRequestRegisterSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        } else {
                            ToastHelper.showToast("提交失败");
                        }
                    }
                });
    }

    @Override
    public void requestAgainSub(String phone, String title, String contact, String orgMobile) {
        if (TextUtils.isEmpty(phone)) {
            ToastHelper.showToast("电话号码不能为空");
            return;
        }

        if (TextUtils.isEmpty(title)) {
            ToastHelper.showToast("机构名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            ToastHelper.showToast("机构联系人不能为空");
            return;
        }
        if (TextUtils.isEmpty(orgMobile)) {
            ToastHelper.showToast("机构联系电话不能为空");
            return;
        }
        if (TextUtils.isEmpty(mCurrentSelectImage1)) {
            ToastHelper.showToast("请选择机构证件");
            return;
        }
        if (TextUtils.isEmpty(mCurrentSelectImage2)) {
            ToastHelper.showToast("请选择机构证件");
            return;
        }

        //上传图片
        Observable<String> uploadUrl = createUploadUrl(mCurrentSelectImage1);
        Observable<String> uploadUrl1 = createUploadUrl(mCurrentSelectImage2);
        Observable
                .zip(uploadUrl, uploadUrl1, new BiFunction<String, String, List<String>>() {
                    @NonNull
                    @Override
                    public List<String> apply(@NonNull String s, @NonNull String s2) throws Exception {
                        List<String> mUris = new ArrayList<>();
                        mUris.add(s);
                        mUris.add(s2);
                        return mUris;
                    }
                })
                .flatMap(new Function<List<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull List<String> strings) throws Exception {
                        return RxHttp.postForm(Configuration.mechanism.KEY_JG_REGISTER1)
                                .add("title", title)
                                .add("orgMobile", orgMobile)
                                .add("contact", contact)
                                .add("foreImg", strings.get(0))
                                .add("tailImg", strings.get(1))
                                .add("phone", phone)
                                .add("userType", "4")
                                .asXXResponse(String.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String loginBean) throws Exception {
                        ToastHelper.showToast("提交成功");
                        getViewer().onRequestRegisterSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        } else {
                            ToastHelper.showToast("提交失败");
                        }
                    }
                });
    }

    public void requestAgencyInfo(String phone){
        RxHttp.postForm(Configuration.mechanism.KEY_TY_RECODE)
                .add("phone", phone)
                .add("userType", "4")
                .asXXResponse(AgencyInfoBean.class)
          .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(new Consumer<AgencyInfoBean>() {
                    @Override
                    public void accept(AgencyInfoBean loginBean) throws Exception {
                        getViewer().onRequestAgencyInfo(loginBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        }
                    }
                });
    }
}
