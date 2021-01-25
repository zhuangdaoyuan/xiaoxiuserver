package com.xiuxiu.confinement_nurse.ui.base.mvp;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.lang.ref.WeakReference;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AndroidModelPresenter<T extends Viewer> extends AndroidViewModel {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected WeakReference<T> viewer;


    public AndroidModelPresenter(@NonNull Application application) {
        super(application);

    }
    public void setViewer(T t){
        this.viewer = new WeakReference<>(t);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public T getViewer() throws NullPointerException {
        if (viewer.get() == null) {
            throw new NullPointerException("viewer 为空");
        }
        return viewer.get();
    }

    public boolean add(@NonNull Disposable disposable) {
        return compositeDisposable.add(disposable);
    }
}
