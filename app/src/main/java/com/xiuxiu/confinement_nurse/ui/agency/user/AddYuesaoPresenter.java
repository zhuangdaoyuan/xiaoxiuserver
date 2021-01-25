package com.xiuxiu.confinement_nurse.ui.agency.user;


import android.net.Uri;
import android.text.TextUtils;
import android.view.TextureView;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.cache.GonAccessorImpl;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.model.event.UserInfoEvent;
import com.xiuxiu.confinement_nurse.ui.agency.vm.MechanismManBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttpFormParam;

public class AddYuesaoPresenter extends BasePresenter<AddYuesaoContract.IView> implements AddYuesaoContract.IPresenter {

    private UserInfoBean yuesaoInfoBean;
    private String url;

    public AddYuesaoPresenter(AddYuesaoContract.IView viewer) {
        super(viewer);
        yuesaoInfoBean = new UserInfoBean();
    }


    @Override
    public void requestUpdateUserInfo() {
        Observable<String> observable;
        if (TextUtils.isEmpty(url)) {
            observable = Observable.just("");
        } else {
            observable = createUploadUrl(url);
        }
        observable.doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                yuesaoInfoBean.setAvatarList(s);
            }
        })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        return createUploadUserInfo(yuesaoInfoBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("提交成功");
                    getViewer().onRequestOK();

                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                });
    }

    @Override
    public void requestUploadFile(List<String> paths, List<Uri> uris) {
        url = CollectionUtil.getSafe(paths, 0, "");

    }


    private Observable<String> createUploadUserInfo(UserInfoBean userInfoBean) {
        String json = GonAccessorImpl.getOriginalGson().toJson(userInfoBean);
        Map map = GonAccessorImpl.getOriginalGson().fromJson(json, Map.class);
        RxHttpFormParam rxHttpFormParam = RxHttp.postForm(Configuration.mechanism.KEY_YUESAO_INFO);
        createMapParam("userName", map, rxHttpFormParam);
        createMapParam("sex", map, rxHttpFormParam);
        createMapParam("birthday", map, rxHttpFormParam);
        createMapParam("nativePlace", map, rxHttpFormParam);
        createMapParam("location", map, rxHttpFormParam);
        createMapParam("address", map, rxHttpFormParam);
        createMapParam("userAvatar", map, rxHttpFormParam);
        createMapParam("avatarList", map, rxHttpFormParam);
        createMapParam("idNo", map, rxHttpFormParam);
        createMapParam("workYears", map, rxHttpFormParam);
        createMapParam("matronId", map, rxHttpFormParam);
        createMapParam("id",map,rxHttpFormParam);

        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
        rxHttpFormParam.add("orgId", userBean.getId());

        City locationCity = ModelManager.getInstance().getCacheInterface().getLocationCity();
        if (locationCity != null) {
            rxHttpFormParam.add("lng", locationCity.getLongitude());
            rxHttpFormParam.add("lat", locationCity.getLatitude());
        }
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


    @Override
    public void rquestName(String title) {
        yuesaoInfoBean.setUserName(title);
    }

    @Override
    public void requestSex(int i) {
        yuesaoInfoBean.setSex(i);
    }

    @Override
    public void requestBirthday(String stampToDate) {
        yuesaoInfoBean.setBirthday(stampToDate);
    }

    @Override
    public void setNativePlace(String nativePlace) {
        yuesaoInfoBean.setNativePlace(nativePlace);
    }

    @Override
    public void setNativePlaceName(String nativePlaceName) {
        yuesaoInfoBean.setNativePlaceName(nativePlaceName);
    }

    @Override
    public void setIdNo(String idCard) {
        yuesaoInfoBean.setIdNo(idCard);
    }

    @Override
    public void setLocation(String code) {

        yuesaoInfoBean.setLocation(code);
    }

    @Override
    public void setAddress(String name) {
        yuesaoInfoBean.setAddress(name);
    }

    @Override
    public void setUserInfo(UserInfoBean userBean) {
        yuesaoInfoBean = userBean;
        yuesaoInfoBean.setUserAvatar("");
    }
}
