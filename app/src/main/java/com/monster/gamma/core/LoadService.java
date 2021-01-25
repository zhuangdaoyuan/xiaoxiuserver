package com.monster.gamma.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.monster.gamma.callback.GammaCallback;
import com.monster.gamma.callback.SuccessCallback;
import com.xiuxiu.confinement_nurse.ui.citypicker.util.ScreenUtil;

import java.util.List;

/**
 *
 */
public class LoadService<T> {
    private LoadLayout loadLayout;
    private Convertor<T> convertor;

    LoadService(Convertor<T> convertor, TargetContext targetContext, GammaCallback.OnReloadListener onReloadListener,
                Gamma.Builder builder) {
        this.convertor = convertor;
        Context context = targetContext.getContext();
        View oldContent = targetContext.getOldContent();
        ViewGroup.LayoutParams oldLayoutParams = oldContent.getLayoutParams();
        loadLayout = new LoadLayout(context, onReloadListener);
        loadLayout.setupSuccessLayout(new SuccessCallback(oldContent, context, onReloadListener));
        if (targetContext.getParentView() != null) {
            targetContext.getParentView().addView(loadLayout, targetContext.getChildIndex(), oldLayoutParams);
        }
        initCallback(builder);
    }

    private void initCallback(Gamma.Builder builder) {
        List<GammaCallback> callbacks = builder.getCallbacks();
        Class<? extends GammaCallback> defalutCallback = builder.getDefaultCallback();
        if (callbacks != null && callbacks.size() > 0) {
            for (GammaCallback callback : callbacks) {
                loadLayout.setupCallback(callback);
            }
        }
        if (defalutCallback != null) {
            loadLayout.showCallback(defalutCallback);
        }
    }

    public void showSuccess() {
        loadLayout.showCallback(SuccessCallback.class);
    }

    public void showCallback(Class<? extends GammaCallback> callback) {
        loadLayout.showCallback(callback);
    }

    public void showWithConvertor(T t) {
        if (convertor == null) {
            throw new IllegalArgumentException("You haven't set the Convertor.");
        }
        loadLayout.showCallback(convertor.map(t));
    }

    public LoadLayout getLoadLayout() {
        return loadLayout;
    }

    public Class<? extends GammaCallback> getCurrentCallback() {
        return loadLayout.getCurrentCallback();
    }

    /**
     * obtain rootView if you want keep the toolbar in Fragment
     *
     * @since 1.2.2
     * @deprecated
     */
    public LinearLayout getTitleLoadLayout(Context context, ViewGroup rootView, View titleView) {
        LinearLayout newRootView = new LinearLayout(context);
        newRootView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        newRootView.setLayoutParams(layoutParams);
        rootView.removeView(titleView);
        newRootView.addView(titleView);
        newRootView.addView(loadLayout, layoutParams);
        return newRootView;
    }

    /**
     * modify the callback dynamically
     *
     * @param callback  which callback you want modify(layout, event)
     * @param transport a interface include modify logic
     * @since 1.2.2
     */
    public LoadService<T> setCallBack(Class<? extends GammaCallback> callback, Transport transport) {
        loadLayout.setCallBack(callback, transport);
        return this;
    }
}
