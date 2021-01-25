package com.xiuxiu.confinement_nurse.ui.agency;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class LearningExperenceListPresenter extends BasePresenter<LearningExperenceListContract.IView> implements LearningExperenceListContract.IPresenter {
    public LearningExperenceListPresenter(LearningExperenceListContract.IView viewer) {
        super(viewer);
    }


    @Override
    public void requestLoad(String matronId) {
        requestEducationList(matronId)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(otherExperienceVm -> {
                    getViewer().onRequestLearningExperience(otherExperienceVm);
                    getViewer().onRequestPageSuccess();
                }, throwable -> {
                    getViewer().onRequestPageError(0);
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                });
    }
    public Observable<LearningExperienceVm> requestEducationList(String matronId) {
        return RxHttp.postForm(Configuration.mechanism.KEY_EDUCATION_GET)
                .add("ysId",matronId)
                .asXXResponse(LearningExperienceVm.class)
                .onErrorResumeNext(new ObservableSource<LearningExperienceVm>() {
                    @Override
                    public void subscribe(Observer<? super LearningExperienceVm> observer) {
                        observer.onNext(new LearningExperienceVm());
                        observer.onComplete();
                    }
                });
    }
}
