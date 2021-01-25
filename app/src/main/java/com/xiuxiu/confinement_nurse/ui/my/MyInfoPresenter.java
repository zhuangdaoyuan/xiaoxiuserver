package com.xiuxiu.confinement_nurse.ui.my;


import android.net.Uri;
import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.cache.GonAccessorImpl;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.model.event.UserInfoEvent;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttpFormParam;

public class MyInfoPresenter extends BasePresenter<MyInfoContract.IView> implements MyInfoContract.IPresenter {
    private String ysid;

    public String getYsid() {
        return ysid;
    }

    public void setYsid(String ysid) {
        this.ysid = ysid;
    }

    public MyInfoPresenter(MyInfoContract.IView viewer) {
        super(viewer);
    }


    public void requestUserInfo(){

    }


    @Override
    public void requestUpdateUserInfo(UserInfoBean userBean, XFunc0 xFunc0) {
        createUploadUserInfo(userBean)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    xFunc0.call();
                    ToastHelper.showToast("更新成功");
                }, throwable -> {
                });
    }

    @Override
    public void requestUploadFile(List<String> paths, List<Uri> uris) {
        String path1 = CollectionUtil.getSafe(paths, 0, "");

        Observable<String> pathObservable1 = createUploadUrl(path1);
        pathObservable1
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String strings) throws Exception {
                        UserInfoBean userInfoBean = new UserInfoBean();
                        userInfoBean.setUserAvatar(strings);
                        userInfoBean.setAvatarList(strings);
                        return createUploadUserInfo(userInfoBean).map(new Function<String, String>() {
                            @Override
                            public String apply(String s) throws Exception {
                                return strings;
                            }
                        });
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                        userBean.getItem().setUserAvatar(s);
                        ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
                        EventBus.UpdateUserInfoByAvatarList().post(new UserInfoEvent());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.asOnMain(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestUploadFileSuccess(s, uris);
                }, throwable -> {

                });
    }


    private Observable<String> createUploadUserInfo(UserInfoBean userInfoBean) {
        String json = GonAccessorImpl.getOriginalGson().toJson(userInfoBean);
        Map map = GonAccessorImpl.getOriginalGson().fromJson(json, Map.class);
        RxHttpFormParam rxHttpFormParam = RxHttp.postForm(Configuration.User.KEY_USER_INFO);
        createMapParam("userName", map, rxHttpFormParam);
        createMapParam("sex", map, rxHttpFormParam);
        createMapParam("birthday", map, rxHttpFormParam);
        createMapParam("nativePlace", map, rxHttpFormParam);
        createMapParam("location", map, rxHttpFormParam);
        createMapParam("address", map, rxHttpFormParam);
        createMapParam("userAvatar",map,rxHttpFormParam);
        createMapParam("avatarList", map, rxHttpFormParam);
        createMapParam("idNo", map, rxHttpFormParam);
        createMapParam("workYears", map, rxHttpFormParam);
        return rxHttpFormParam
                .addHeader("Content-Type", "application/json")
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void createMapParam(String key, Map map, RxHttpFormParam rxHttpFormParam) {
        Object o = map.get(key);
        if (o instanceof String) {
            String userName = (String) o;
            if (!TextUtils.isEmpty(userName)) {
                rxHttpFormParam.add(key, userName);
            }
        } else if (o instanceof Integer) {
            Integer integer = (Integer) o;
            if (integer != 0) {
                rxHttpFormParam.add(key, integer);
            }
        } else if (o instanceof Double) {
            Double d = (Double) o;
            Integer integer = d.intValue();
            if (integer != 0) {
                rxHttpFormParam.add(key, integer);
            }
        } else if (o instanceof List) {
            List list = (List) o;
            try {
                String[] l = (String[]) list.toArray(new String[list.size()]);
                rxHttpFormParam.add(key, GonAccessorImpl.getOriginalGson().toJson(l));
            } catch (Exception e) {

            }
        }
    }

}
