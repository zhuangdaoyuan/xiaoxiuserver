package com.xiuxiu.confinement_nurse.ui.my;


import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.iceteck.silicompressorr.SiliCompressor;
import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.event.ExperienceEvent;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;

import java.io.File;
import java.net.URISyntaxException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class SelfEvaluationPresenter extends BasePresenter<SelfEvaluationContract.IView> implements SelfEvaluationContract.IPresenter {

    public SelfEvaluationPresenter(SelfEvaluationContract.IView viewer) {
        super(viewer);
    }
    private String ysId;

    public String getYsId() {
        return ysId;
    }

    public void setYsId(String ysId) {
        this.ysId = ysId;
    }

    @Override
    public void requestSubmit(Context context, String path, String toString) {
        Observable<String> uploadUrl;
        if (!TextUtils.isEmpty(path)) {
            uploadUrl = Observable
                    .just(path)
                    .observeOn(Schedulers.io())
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) throws Exception {
                            String f = getDefaultCachePathForSystem(context);
                            return SiliCompressor.with(context).compressVideo(path, f);
                        }
                    })
                    .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
                        @Override
                        public ObservableSource<? extends String> apply(Throwable throwable) throws Exception {
                            return Observable.just(path);
                        }
                    })
                    .flatMap(new Function<String, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(String s) throws Exception {
                            return createUploadVideoUrl(s);
                        }
                    });
        } else {
            uploadUrl = Observable.just("");
        }
        uploadUrl
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        if (TextUtils.isEmpty(ysId)) {
                            return RxHttp.postForm(Configuration.User.KEY_SELF_TERMINATION_GET)
                                    .add("content", toString)
                                    .add("video", s)
                                    .asXXResponse(String.class);
                        }else{
                            return RxHttp.postForm(Configuration.mechanism.KEY_SELF_TERMINATION_POST)
                                    .add("content", toString)
                                    .add("video", s)
                                    .add("ysId",ysId)
                                    .asXXResponse(String.class);
                        }

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("提交成功");
                    EventBus.SelfEvaluation().post(new ExperienceEvent());
                    getViewer().onRequestFinish();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    } else {
                        ToastHelper.showToast("提交失败");
                    }
                });
    }

    public String getDefaultCachePathForSystem(Context context) {
        String type = Environment.DIRECTORY_DOWNLOADS;
        String directoryPath;
        //判断SD卡是否可用
        if (isSdExist()) {
            File file = context.getExternalFilesDir(type);
            if (file == null) {
                directoryPath = context.getFilesDir() + File.separator + type;
            } else {
                directoryPath = file.getAbsolutePath();
            }
        } else {
            //没内存卡就存机身内存
            directoryPath = context.getFilesDir() + File.separator + type;
            // directoryPath=context.getCacheDir()+File.separator+dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {
            //判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }

    private boolean isSdExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
