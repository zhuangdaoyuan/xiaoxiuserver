package com.xiuxiu.confinement_nurse.ui.my;


import android.text.TextUtils;
import android.util.Log;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.event.ExperienceEvent;
import com.xiuxiu.confinement_nurse.ui.my.vm.OtherExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.PathVm;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttpFormParam;
import rxhttp.wrapper.utils.GsonUtil;

public class OtherExperiencePresenter extends ExperiencePresenter<OtherExperienceContract.IView> implements OtherExperienceContract.IPresenter {

    public OtherExperiencePresenter(OtherExperienceContract.IView viewer) {
        super(viewer);
    }

    @Override
    protected void onRequestFinish() {
        getViewer().onRequestFinish();
    }

    private String ysId;

    public String getYsId() {
        return ysId;
    }

    public void setYsId(String ysId) {
        this.ysId = ysId;
    }

    @Override
    public void requestData() {

        List<FilterRadioVm> list = new ArrayList<>();
        FilterRadioVm filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("月嫂服务");
        filterRadioVm.setCode(1);
        list.add(filterRadioVm);

        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("育婴师服务");
        filterRadioVm.setCode(2);
        list.add(filterRadioVm);

        getViewer().onRequestService(list);


        list = new ArrayList<>();
        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("单胎新生儿");
        filterRadioVm.setCode(1);
        list.add(filterRadioVm);

        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("双胎新生儿");
        filterRadioVm.setCode(2);
        list.add(filterRadioVm);

        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("0~1岁幼儿");
        filterRadioVm.setCode(3);
        list.add(filterRadioVm);


        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("1岁以上幼儿");
        filterRadioVm.setCode(4);
        list.add(filterRadioVm);

        getViewer().onRequestType(list);

        getViewer().onRequestData();
    }

    @Override
    public void requestSubmit(List<PathVm> path, OtherExperienceVm.OtherExperience learningExperience) {
        requestSubmit(path, "", learningExperience);
    }

    @Override
    public void requestSubmit(List<PathVm> path, String id, OtherExperienceVm.OtherExperience learningExperience) {
        if (path.isEmpty()) {
            return;
        }
        Observable.fromIterable(path)
                .flatMap(new Function<PathVm, ObservableSource<PathVm>>() {
                    @Override
                    public ObservableSource<PathVm> apply(PathVm pathVm) throws Exception {
                        if (TextUtils.isEmpty(pathVm.path)) {
                            return Observable.just(pathVm);
                        }
                        Observable<String> uploadUrl = createUploadUrl(pathVm.path);
                        return uploadUrl.map(new Function<String, PathVm>() {
                            @Override
                            public PathVm apply(String s) throws Exception {
                                pathVm.url=s;
                                return pathVm;
                            }
                        });
                    }
                })
                .toList()
                .toObservable()
                .map(new Function<List<PathVm>, List<String>>() {
                    @Override
                    public List<String> apply(List<PathVm> pathVms) throws Exception {
                        List<String> images=new ArrayList<>();
                        for (int i = 0; i < pathVms.size(); i++) {
                            PathVm str = pathVms.get(i);
                            if (!TextUtils.isEmpty(str.url)) {
                                images.add(str.url);
                            }
                        }
                        if (images.size()!=0) {
                            learningExperience.setImages(GsonUtil.toJson(images));
                        }
                        return images;
                    }
                })
                .flatMap(new Function<List<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(List<String> strings) throws Exception {
                        RxHttpFormParam rxHttpFormParam;
                        if (TextUtils.isEmpty(id)) {
                            if (TextUtils.isEmpty(ysId)) {
                                rxHttpFormParam = RxHttp.postForm(Configuration.User.KEY_OTHER_ADD);
                            }else{
                                rxHttpFormParam = RxHttp.postForm(Configuration.mechanism.KEY_OTHER_ADD)
                                        .add("ysId",ysId);
                            }
                        } else {
                            if (TextUtils.isEmpty(ysId)) {
                                rxHttpFormParam = RxHttp.postForm(Configuration.User.KEY_OTHER_UPDATE)
                                        .add("id",id);
                            }else{
                                rxHttpFormParam = RxHttp.postForm(Configuration.mechanism.KEY_OTHER_UPDATE)
                                        .add("id",id)
                                        .add("ysId",ysId);
                            }

                        }
                        return rxHttpFormParam
                                .add("serviceType", learningExperience.getServiceType())
                                .add("objectType", learningExperience.getObjectType())
                                .add("groupType", learningExperience.getGroupType())
                                .add("groupName", learningExperience.getGroupName())
                                .add("location", learningExperience.getLocation())
                                .add("serviceStartTime", learningExperience.getServiceEndTime())
                                .add("serviceEndTime", learningExperience.getServiceStartTime())
                                .add("images",GsonUtil.toJson(strings))
                                .asXXResponse(String.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestSubmitSuccess();
                    EventBus.OtherExperience().post(new ExperienceEvent());
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
    public void requestDelete(String id) {
        Observable<String> observable;
        if (TextUtils.isEmpty(ysId)) {
            observable = RxHttp.postForm(Configuration.User.KEY_OTHER_DECTED)
                    .add("id", id)
                    .asXXResponse(String.class);
        }else {
            observable = RxHttp.postForm(Configuration.mechanism.KEY_OTHER_DECTED)
                    .add("id", id)
                    .add("ysId",ysId)
                    .asXXResponse(String.class);
        }
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    EventBus.OtherExperience().post(new ExperienceEvent());
                    ToastHelper.showToast("删除成功");
                    getViewer().onRequestFinish();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    } else {
                        ToastHelper.showToast("删除失败");
                    }
                });
    }
}
