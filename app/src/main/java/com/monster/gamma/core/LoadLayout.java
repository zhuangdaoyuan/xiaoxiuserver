package com.monster.gamma.core;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.monster.gamma.GammaUtil;
import com.monster.gamma.callback.GammaCallback;
import com.monster.gamma.callback.SuccessCallback;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class LoadLayout extends FrameLayout {
    private Map<Class<? extends GammaCallback>, GammaCallback> callbacks = new HashMap<>();
    private Context context;
    private GammaCallback.OnReloadListener onReloadListener;
    private Class<? extends GammaCallback> preCallback;
    private Class<? extends GammaCallback> curCallback;
    private static final int CALLBACK_CUSTOM_INDEX = 1;

    public LoadLayout(@NonNull Context context) {
        super(context);
    }

    public LoadLayout(@NonNull Context context, GammaCallback.OnReloadListener onReloadListener) {
        this(context);
        this.context = context;
        this.onReloadListener = onReloadListener;
        setClipChildren(false);
    }

    public void setupSuccessLayout(GammaCallback callback) {
        addCallback(callback);
        View successView = callback.getRootView();
        successView.setVisibility(View.GONE);
        addView(successView);
        curCallback = SuccessCallback.class;
    }

    public void setupCallback(GammaCallback callback) {
        GammaCallback cloneCallback = callback.copy();
        cloneCallback.setCallback(null, context, onReloadListener);
        addCallback(cloneCallback);
    }

    public void addCallback(GammaCallback callback) {
        if (!callbacks.containsKey(callback.getClass())) {
            callbacks.put(callback.getClass(), callback);
        }
    }

    public void showCallback(final Class<? extends GammaCallback> callback) {
        showCallback(callback, null);
    }

    public void showCallback(final Class<? extends GammaCallback> callback, Bundle bundle) {
        checkCallbackExist(callback);
        if (GammaUtil.isMainThread()) {
            showCallbackView(callback, bundle);
        } else {
            postToMainThread(callback, bundle);
        }
    }

    public Class<? extends GammaCallback> getCurrentCallback() {
        return curCallback;
    }

    private void postToMainThread(final Class<? extends GammaCallback> status, Bundle bundle) {
        post(new Runnable() {
            @Override
            public void run() {
                showCallbackView(status, bundle);
            }
        });
    }


    private void showCallbackView(Class<? extends GammaCallback> status, Bundle bundle) {
        if (preCallback != null) {
            if (preCallback == status) {
                return;
            }
            callbacks.get(preCallback).onDetach();
        }
        if (getChildCount() > 1) {
            removeViewAt(CALLBACK_CUSTOM_INDEX);
        }

        for (Class key : callbacks.keySet()) {
            if (key == status) {
                SuccessCallback successCallback = (SuccessCallback) callbacks.get(SuccessCallback.class);
                if (key == SuccessCallback.class) {
                    successCallback.show();
                } else {
                    successCallback.showWithCallback(callbacks.get(key).getSuccessVisible());
                    View rootView = callbacks.get(key).getRootView();
                    addView(rootView);
                    if (null != bundle) {
                        callbacks.get(key).onAttach(context, rootView, bundle);
                    } else {
                        callbacks.get(key).onAttach(context, rootView);
                    }
                }
                preCallback = status;
            }
        }
        curCallback = status;
    }


    public void setCallBack(Class<? extends GammaCallback> callback, Transport transport) {
        if (transport == null) {
            return;
        }
        checkCallbackExist(callback);
        transport.order(context, callbacks.get(callback).obtainRootView());
    }

    private void checkCallbackExist(Class<? extends GammaCallback> callback) {
        if (!callbacks.containsKey(callback)) {
            throw new IllegalArgumentException(String.format("The GammaCallback (%s) is nonexistent.", callback
                    .getSimpleName()));
        }
    }
}
