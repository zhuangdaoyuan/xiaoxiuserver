package com.xiuxiu.confinement_nurse.ui.base.mvp;


import android.text.TextUtils;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.http.bean.login.UserAvatarBean;
import com.xiuxiu.confinement_nurse.ui.MainActivity;
import com.xiuxiu.confinement_nurse.utlis.BitmapUtil;
import com.xiuxiu.confinement_nurse.utlis.Utils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rxhttp.wrapper.param.RxHttp;
import top.zibin.luban.Luban;

public class BasePresenter<T extends Viewer> implements LifecycleObserver {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected WeakReference<T> viewer;

    public BasePresenter(T viewer) {
        this.viewer = new WeakReference<>(viewer);
        if (viewer != null) {
            viewer.getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (viewer.get() != null) {
            Lifecycle lifecycle = viewer.get().getLifecycle();
            lifecycle.removeObserver(this);
        }
        compositeDisposable.dispose();
    }

    public T getViewer() throws NullPointerException {
        if (viewer.get() == null) {
            //通过获得泛型类的父类，拿到泛型的接口类实例，通过反射来实例化 model
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            if (type != null) {
                Type[] types = type.getActualTypeArguments();
                try {
                    T Viewer = (T) ((Class<?>) types[0]).newInstance();
                    this.viewer = new WeakReference<T>(Viewer);
                    return viewer.get();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return viewer.get();
    }

    public boolean add(@NonNull Disposable disposable) {
        return compositeDisposable.add(disposable);
    }


    public Observable<List<String>> createUploadUrl(List<String> path) {
      return Observable.fromIterable(path)
               .flatMap(new Function<String, ObservableSource<String>>() {
                   @Override
                   public ObservableSource<String> apply(String s) throws Exception {
                       if (TextUtils.isEmpty(s)) {
                           return Observable.just("");
                       }
                       return createUploadUrl(s);
                   }
               })
               .toList()
               .toObservable();

    }
    public Observable<String> createUploadUrl(String path){

        Observable<String> pathObservable1;
        if (!TextUtils.isEmpty(path)) {
            pathObservable1 =    Observable.just(path)
                    .observeOn(Schedulers.io())
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) throws Exception {
                            List<File> files = Luban.with(Utils.getApp()).load(new ArrayList<String>() {{
                                add(s);
                            }}).get();
                            if (files.isEmpty()) {
                                return"";
                            }
                            return files.get(0).getAbsolutePath();
                        }
                    })
                    .flatMap(new Function<String, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(String s) throws Exception {
                            File file = new File(s);
                            return    RxHttp.postForm(Configuration.KEY_UPLOAD)
                                    .addFile("file", file)
                                    .asXXResponse(UserAvatarBean.class)
                                    .map(new Function<UserAvatarBean, String>() {
                                        @Override
                                        public String apply(UserAvatarBean userAvatarBean) throws Exception {
                                            return userAvatarBean.getUrl();
                                        }
                                    });
                        }
                    });
        } else {
            pathObservable1 = Observable.just("");
        }
        return pathObservable1;
    }

    public Observable<String> createUploadVideoUrl(String path){

        Observable<String> pathObservable1;
        if (!TextUtils.isEmpty(path)) {
            pathObservable1 =    Observable.just(path)
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<String, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(String s) throws Exception {
                            File file = new File(s);
                            return    RxHttp.postForm(Configuration.KEY_UPLOAD)
                                    .addFile("file", file)
                                    .asXXResponse(UserAvatarBean.class)
                                    .map(new Function<UserAvatarBean, String>() {
                                        @Override
                                        public String apply(UserAvatarBean userAvatarBean) throws Exception {
                                            return userAvatarBean.getUrl();
                                        }
                                    });
                        }
                    });
        } else {
            pathObservable1 = Observable.just("");
        }
        return pathObservable1;
    }
}
