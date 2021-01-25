package com.xiuxiu.confinement_nurse.ui.my;


import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.event.ExperienceEvent;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttpFormParam;

public class LearningExperiencePresenter extends BasePresenter<LearningExperienceContract.IView> implements LearningExperienceContract.IPresenter {

    private String ysId;
    @Override
    public void setId(String id) {
        this.ysId = id;
    }

    public LearningExperiencePresenter(LearningExperienceContract.IView viewer) {
        super(viewer);
    }

    @Override
    public void requestLearningData() {

        List<FilterRadioVm> list = new ArrayList<>();

        FilterRadioVm filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("博士");
        filterRadioVm.setCode(8);
        list.add(filterRadioVm);

        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("研究生");
        filterRadioVm.setCode(7);
        list.add(filterRadioVm);

        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("本科");
        filterRadioVm.setCode(6);
        list.add(filterRadioVm);


        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("专科");
        filterRadioVm.setCode(5);
        list.add(filterRadioVm);


        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("中专");
        filterRadioVm.setCode(4);
        list.add(filterRadioVm);


        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("高中");
        filterRadioVm.setCode(3);
        list.add(filterRadioVm);

        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("初中");
        filterRadioVm.setCode(2);
        list.add(filterRadioVm);

        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("小学");
        filterRadioVm.setCode(1);
        list.add(filterRadioVm);

        filterRadioVm = new FilterRadioVm();
        filterRadioVm.setState(0);
        filterRadioVm.setTitle("其他");
        filterRadioVm.setCode(0);
        list.add(filterRadioVm);

        getViewer().onRequestLearningData(list);

        getViewer().onRequestDataEnd();
    }


    @Override
    public void requestDelete(String id) {
        if (TextUtils.isEmpty(this.ysId)) {
            RxHttp.postForm(Configuration.User.KEY_EDUCATION_DELETE)
                    .add("id", id)
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        ToastHelper.showToast("删除成功");
                        EventBus.LearningExperience().post(new ExperienceEvent());
                        getViewer().onRequestFinish();
                    }, throwable -> {
                        ToastHelper.showToast("删除失败");
                    });
        }else{
            RxHttp.postForm(Configuration.mechanism.KEY_EDUCATION_DELETE)
                    .add("id", id)
                    .add("ysId",ysId)
                    .asXXResponse(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                    .doFinally(() -> getViewer().cancelLoadingDialog())
                    .doOnError(throwable -> getViewer().cancelLoadingDialog())
                    .as(RxLife.as(getViewer()))
                    .subscribe(s -> {
                        ToastHelper.showToast("删除成功");
                        EventBus.LearningExperience().post(new ExperienceEvent());
                        getViewer().onRequestFinish();
                    }, throwable -> {
                        if(throwable instanceof ParseException){
                            ToastHelper.showToast(throwable.getMessage());
                        }

                    });
        }

    }

    @Override
    public void requestSubmit(String path, LearningExperienceVm.LearningExperience learningExperience) {
        String image = learningExperience.getImage();
        Observable<String> uploadUrl;
        if (!TextUtils.isEmpty(image)) {
            uploadUrl = Observable.just(image);
        } else {
            uploadUrl = createUploadUrl(path);
        }
        uploadUrl
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String s) throws Exception {
                        String id = learningExperience.getId();
                        RxHttpFormParam rxHttpFormParam;
                        if (TextUtils.isEmpty(ysId)) {
                            if (TextUtils.isEmpty(id)) {
                                rxHttpFormParam = RxHttp.postForm(Configuration.User.KEY_EDUCATION_ADD);
                            } else {
                                rxHttpFormParam = RxHttp.postForm(Configuration.User.KEY_EDUCATION_UPDATE).add("id", id);
                            }
                        }else{
                            if (TextUtils.isEmpty(id)) {
                                rxHttpFormParam = RxHttp.postForm(Configuration.mechanism.KEY_EDUCATION_ADD)
                                .add("ysId",ysId)
                                ;
                            } else {
                                rxHttpFormParam = RxHttp.postForm(Configuration.mechanism.KEY_EDUCATION_UPDATE).add("id", id).add("ysId",ysId);
                            }
                        }

                        RxHttpFormParam add = rxHttpFormParam.add("type", learningExperience.getType())
                                .add("image", s)
                                .add("startTime", learningExperience.getStartTime())
                                .add("school", learningExperience.getSchool())
                                .add("endTime", learningExperience.getEndTime());
                        if (!TextUtils.isEmpty(ysId)){
                            add.add("ysId",ysId);
                        }
                            if (TextUtils.equals(learningExperience.getType(), "1")) {
                            rxHttpFormParam.add("level", learningExperience.getLevel());
                            rxHttpFormParam.add("subject", learningExperience.getSubject());
                        } else if (TextUtils.equals(learningExperience.getType(), "2")) {
                            rxHttpFormParam.add("degree", learningExperience.getDegree());
                        }
                        return rxHttpFormParam.asXXResponse(String.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("提交成功");
                    EventBus.LearningExperience().post(new ExperienceEvent());
                    getViewer().onRequestFinish();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    } else {
                        ToastHelper.showToast("提交失败");
                    }
                });
    }

}
