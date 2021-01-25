package com.xiuxiu.confinement_nurse.ui.base.mvp;


import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class ViewerImpl implements Viewer, LifecycleObserver {

    @NonNull
    private LifecycleOwner lifecycleOwner;

    public ViewerImpl(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        lifecycleOwner.getLifecycle().removeObserver(this);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleOwner.getLifecycle();
    }
}
