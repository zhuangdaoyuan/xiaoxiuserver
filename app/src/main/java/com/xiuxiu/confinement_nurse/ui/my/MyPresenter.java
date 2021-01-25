package com.xiuxiu.confinement_nurse.ui.my;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.collection.LruCache;
import androidx.core.widget.NestedScrollView;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.IntCodeEnum;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.CertificateInfoBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.base.view.consecutivescroller.ConsecutiveScrollerLayout;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.OtherExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.SelfExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.ServiceExperienceVm;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import org.reactivestreams.Subscription;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function5;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttpFormParam;

public class MyPresenter extends BasePresenter<MyContract.IView> implements MyContract.IPresenter {

    public MyPresenter(MyContract.IView viewer) {
        super(viewer);
        initData();
    }

    private String ysId;

    @Override
    public void setYsId(String ysId) {
        this.ysId = ysId;
    }
    @Override
    public String getYsId(){
        return ysId;
    }
    /**
     * 上报 身份认证信息
     *
     * @param requestCode
     * @param uri
     * @param path
     */
    @Override
    public void requestUploadCertification(int requestCode, Uri uri, String path, String idCard) {
        String msg;
        if (requestCode == 1) {
            msg = IntCodeEnum.KEY_ID_CARD;
        } else if (requestCode == 2) {
            msg = IntCodeEnum.KEY_FACE_ID;
        } else if (requestCode == 3) {
            msg = IntCodeEnum.KEY_HEALTH_CERT;
        } else if (requestCode == 4) {
            msg = IntCodeEnum.KEY_NO_CRIMINAL_CERT;
        } else if (requestCode == 5) {
            msg = IntCodeEnum.KEY_MATRON_CERT;
        } else if (requestCode == 6) {
            msg = IntCodeEnum.KEY_NURSEMAID_CERT;
        } else {
            return;
        }
        createUploadUrl(path)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String strings) throws Exception {
                        RxHttpFormParam add;
                        if (TextUtils.isEmpty(ysId)) {
                            add = RxHttp.postForm(Configuration.User.KEY_CERTIFICATE_UPLOAD_INFO)
                                    .add("authType", msg)
                                    .add("img", strings);
                        }else{
                            add = RxHttp.postForm(Configuration.mechanism.KEY_CERTIFICATE_UPLOAD_INFO)
                                    .add("ysId",ysId)
                                    .add("authType", msg)
                                    .add("img", strings);
                        }
                        if (requestCode == 1) {
                            add.add("extra", idCard);
                        }
                        return add
                                .asXXResponse(String.class);

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    ToastHelper.showToast("提交成功");
                    getViewer().onRequsetCertification(requestCode, uri);
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    } else {
                        ToastHelper.showToast("提交失败");
                    }
                });
    }

    @Override
    public void requestLoadOtherList() {
        requestOtherList()
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(otherExperienceVm -> {
                    getViewer().onRequestOtherExperienceVm(otherExperienceVm);
                }, throwable -> {

                });
    }

    @Override
    public void requestLoadEducationList() {
        requestEducationList()
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(otherExperienceVm -> {
                    getViewer().onRequestLearningExperience(otherExperienceVm);
                }, throwable -> {

                });
    }

    int ssssssss = 0;
    LruCache<String, Bitmap> bitmaCache;
    Disposable disposable;

    PublishProcessor<Integer> rxEventPublishSubject = PublishProcessor.create();
    Subscription subscription;

    private void initData() {
        rxEventPublishSubject
                .onBackpressureBuffer()
                .subscribe(new FlowableSubscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(Integer s) {
                        add(Observable.just(s)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext(new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer aLong) throws Exception {
                                        consecutiveScrollerLayoutWeakReference.get().scrollTo(0, (int) (ssssssss * aLong));
                                        Log.i("xxxxxxxxxxx", "2----2-->" + ssssssss);
                                    }
                                })
                                .observeOn(Schedulers.computation())
                                .map(new Function<Integer, Bitmap>() {
                                    @Override
                                    public Bitmap apply(Integer aLong) throws Exception {
                                        ViewGroup linearLayout = linearLayoutWeakReference.get();
                                        return ViewHelper.changeColor(ViewHelper.loadBitmapFromViewBySystem(linearLayout));
                                    }
                                })
                                .doOnNext(new Consumer<Bitmap>() {
                                    @Override
                                    public void accept(Bitmap bitmap) throws Exception {
                                        bitmaCache.put(String.valueOf(ssssssss), bitmap);
                                    }
                                })
                                .subscribe(new Consumer<Bitmap>() {
                                    @Override
                                    public void accept(Bitmap bitmap) throws Exception {
                                        Log.i("xxxxxxxxxxx", "2----3-->" + ssssssss);
                                        if (ssssssss >= size) {
                                            countDownLatch.countDown();
                                        } else {
                                            subscription.request(1);
                                            ssssssss++;
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        countDownLatch.countDown();
                                    }
                                }));

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private WeakReference<ViewGroup> linearLayoutWeakReference;
    private WeakReference<ConsecutiveScrollerLayout> consecutiveScrollerLayoutWeakReference;
    private CountDownLatch countDownLatch;
    private int size;

    @Override
    public void createUploadUrl(ViewGroup linearLayout, ConsecutiveScrollerLayout activityMyBg, XFunc1<String> callback, XFunc0 errorCallBack) {
        linearLayoutWeakReference = new WeakReference<>(linearLayout);
        consecutiveScrollerLayoutWeakReference = new WeakReference<>(activityMyBg);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        if (bitmaCache != null) {
            bitmaCache.evictAll();
        }
        bitmaCache = new LruCache<>(cacheSize);


        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
//                        countDownLatch = new CountDownLatch(1);
//
//                        ssssssss = 0;
//
//                        int height = linearLayout.getHeight();
//                        Log.i("xxxxxxxxxxx", "1-------->" + height);
//                        int h = 0;
//                        for (int i = 0; i < activityMyBg.getChildCount(); i++) {
//                            h += activityMyBg.getChildAt(i).getHeight();
//                        }
//
//                        size = h / height;
//                        int i2 = h % height;
//
//                        for (int i = 0; i <= size; i++) {
//                            rxEventPublishSubject.onNext(height);
//                        }
//                        Log.i("xxxxxxxxxxx", "2----->" + size);
//
//                        countDownLatch.await();
//                        Log.i("xxxxxxxxxxx", "3");
//                        Bitmap bitmap1 = ViewHelper.mergeBitmap(bitmaCache.get("0"), bitmaCache.get("1"), bitmaCache.get("2"));
//                        Log.i("xxxxxxxxxxx", "４");

                        return ViewHelper.changeColor(ViewHelper.getBitmapByView((NestedScrollView) linearLayout));
                    }
                })
                .map(new Function<Bitmap, String>() {
                    @Override
                    public String apply(Bitmap bitmap) throws Exception {
                        Log.i("xxxxxxxxxxx", "５");
                        return ViewHelper.saveImageToGallery(activityMyBg.getContext(), bitmap);
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        Log.i("xxxxxxxxxxx", "６");
                        return Observable.just(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    Log.i("xxxxxxxxxxx", "7");
                    callback.call(s);
                }, throwable -> {
                    throwable.printStackTrace();
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                    Log.i("xxxxxxxxxxx", "8");
                    errorCallBack.call();
                });
    }


    @Override
    public void requestLoadSelfExperience() {
        requestSelfExperience()
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(otherExperienceVm -> {
                    getViewer().onRequestSelfExperience(otherExperienceVm);
                }, throwable -> {

                });
    }


    @Override
    public void requestLoadData() {
        getViewer().onRequestLoading();
        Observable<LearningExperienceVm> learningExperienceVmObservable = requestEducationList();
        Observable<OtherExperienceVm> otherExperienceVmObservable = requestOtherList();
        Observable<ServiceExperienceVm> serviceExperienceVmObservable = requestServiceExperienceVm();
        Observable<SelfExperienceVm> selfExperienceVmObservable = requestSelfExperience();
        Observable<CertificateInfoBean> certificateInfoBeanObservable = requestUserInfoByCertificate();
        Observable
                .zip(learningExperienceVmObservable, otherExperienceVmObservable, serviceExperienceVmObservable, selfExperienceVmObservable
                        , certificateInfoBeanObservable,
                        new Function5<LearningExperienceVm, OtherExperienceVm, ServiceExperienceVm, SelfExperienceVm, CertificateInfoBean, Object[]>() {
                            @Override
                            public Object[] apply(LearningExperienceVm learningExperienceVm, OtherExperienceVm otherExperienceVm, ServiceExperienceVm serviceExperienceVm,
                                                  SelfExperienceVm selfExperienceVm, CertificateInfoBean certificateInfoBean) throws Exception {
                                return new Object[]{learningExperienceVm, otherExperienceVm, serviceExperienceVm, selfExperienceVm, certificateInfoBean};
                            }
                        })
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestLearningExperience((LearningExperienceVm) s[0]);
                    getViewer().onRequestOtherExperienceVm((OtherExperienceVm) s[1]);
                    getViewer().onRequestServiceExperience((ServiceExperienceVm) s[2]);
                    getViewer().onRequestSelfExperience((SelfExperienceVm) s[3]);
                    getViewer().onRequestSelfExperience((SelfExperienceVm) s[3]);
                    getViewer().loadUserCertificateInfoState((CertificateInfoBean) s[4]);
                    getViewer().onRequestPageSuccess();
                }, throwable -> {
                    throwable.printStackTrace();
                    getViewer().onRequestPageError(0);
                });
    }

    /**
     * 获取学习经历
     *
     * @return
     */
    public Observable<LearningExperienceVm> requestEducationList() {
        if (TextUtils.isEmpty(ysId)) {
            return RxHttp.get(Configuration.User.KEY_EDUCATION_GET)
                    .asXXResponse(LearningExperienceVm.class)
                    .onErrorResumeNext(new ObservableSource<LearningExperienceVm>() {
                        @Override
                        public void subscribe(Observer<? super LearningExperienceVm> observer) {
                            observer.onNext(new LearningExperienceVm());
                            observer.onComplete();
                        }
                    });
        }
        return RxHttp.postForm(Configuration.mechanism.KEY_EDUCATION_GET)
                .add("ysId",ysId)
                .asXXResponse(LearningExperienceVm.class)
                .onErrorResumeNext(new ObservableSource<LearningExperienceVm>() {
                    @Override
                    public void subscribe(Observer<? super LearningExperienceVm> observer) {
                        observer.onNext(new LearningExperienceVm());
                        observer.onComplete();
                    }
                });
    }

    public Observable<ServiceExperienceVm> requestServiceExperienceVm() {
        if (TextUtils.isEmpty(ysId)) {
            return RxHttp.get(Configuration.User.KEY_ORDER_INFO_GET)
                    .asXXResponse(ServiceExperienceVm.class)
                    .onErrorResumeNext(new ObservableSource<ServiceExperienceVm>() {
                        @Override
                        public void subscribe(Observer<? super ServiceExperienceVm> observer) {
                            observer.onNext(new ServiceExperienceVm());
                            observer.onComplete();
                        }
                    });
        }
        return RxHttp.postForm(Configuration.mechanism.KEY_ORDER_INFO_GET)
                .add("ysId",ysId)
                .asXXResponse(ServiceExperienceVm.class)
                .onErrorResumeNext(new ObservableSource<ServiceExperienceVm>() {
                    @Override
                    public void subscribe(Observer<? super ServiceExperienceVm> observer) {
                        observer.onNext(new ServiceExperienceVm());
                        observer.onComplete();
                    }
                });

    }

    public Observable<OtherExperienceVm> requestOtherList() {
        if (TextUtils.isEmpty(ysId)) {
            return RxHttp.get(Configuration.User.KEY_OTHER_GET)
                    .asXXResponse(OtherExperienceVm.class)
                    .onErrorResumeNext(new ObservableSource<OtherExperienceVm>() {
                        @Override
                        public void subscribe(Observer<? super OtherExperienceVm> observer) {
                            observer.onNext(new OtherExperienceVm());
                            observer.onComplete();
                        }
                    });
        }
        return RxHttp.postForm(Configuration.mechanism.KEY_OTHER_GET)
                .add("ysId",ysId)
                .asXXResponse(OtherExperienceVm.class)
                .onErrorResumeNext(new ObservableSource<OtherExperienceVm>() {
                    @Override
                    public void subscribe(Observer<? super OtherExperienceVm> observer) {
                        observer.onNext(new OtherExperienceVm());
                        observer.onComplete();
                    }
                });
    }

    public Observable<SelfExperienceVm> requestSelfExperience() {
        if (TextUtils.isEmpty(ysId)) {
            return RxHttp.get(Configuration.User.KEY_SELF_TERMINATION_GET)
                    .asXXResponse(SelfExperienceVm.class)
                    .onErrorResumeNext(new ObservableSource<SelfExperienceVm>() {
                        @Override
                        public void subscribe(Observer<? super SelfExperienceVm> observer) {
                            observer.onNext(new SelfExperienceVm());
                            observer.onComplete();
                        }
                    });
        }
        return RxHttp.postForm(Configuration.mechanism.KEY_SELF_TERMINATION_GET)
                .add("ysId",ysId)
                .asXXResponse(SelfExperienceVm.class)
                .onErrorResumeNext(new ObservableSource<SelfExperienceVm>() {
                    @Override
                    public void subscribe(Observer<? super SelfExperienceVm> observer) {
                        observer.onNext(new SelfExperienceVm());
                        observer.onComplete();
                    }
                });
    }


    public Observable<CertificateInfoBean> requestUserInfoByCertificate() {
        if (TextUtils.isEmpty(ysId)) {
            return RxHttp.get(Configuration.User.KEY_CERTIFICATE_INFO)
                    .asXXResponse(CertificateInfoBean.class)
                    .doOnNext(new Consumer<CertificateInfoBean>() {
                        @Override
                        public void accept(CertificateInfoBean certificateInfoBean) throws Exception {
                            UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                            userBean.setCertificateInfoBean(certificateInfoBean);
                        }
                    });
        }
        return RxHttp.postForm(Configuration.mechanism.KEY_CERTIFICATE_INFO)
                .add("ysId",ysId)
                .asXXResponse(CertificateInfoBean.class);

    }


}
