package com.monster.gamma.callback;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.io.*;


/**
 *
 */
public abstract class GammaCallback implements Serializable {
    private View rootView;
    private Context context;
    protected OnReloadListener onReloadListener;
    private boolean successViewVisible;

    public GammaCallback() {
    }

    GammaCallback(View view, Context context, OnReloadListener onReloadListener) {
        this.rootView = view;
        this.context = context;
        this.onReloadListener = onReloadListener;
    }

    public GammaCallback setCallback(View view, Context context, OnReloadListener onReloadListener) {
        this.rootView = view;
        this.context = context;
        this.onReloadListener = onReloadListener;
        return this;
    }

    public View getRootView() {
        int resId = onCreateView();
        if (resId == 0 && rootView != null) {
            return rootView;
        }

        if (onBuildView(context) != null) {
            rootView = onBuildView(context);
        }

        if (rootView == null) {
            rootView = View.inflate(context, onCreateView(), null);
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReloadEvent(context, rootView)) {
                    return;
                }
                if (onReloadListener != null) {
                    onReloadListener.onReload(v);
                }
            }
        });
        onViewCreate(context, rootView);
        return rootView;
    }

    protected View onBuildView(Context context) {
        return null;
    }

    /**
     * if return true, the successView will be visible when the view of callback is attached.
     */
    public boolean getSuccessVisible() {
        return successViewVisible;
    }

    void setSuccessVisible(boolean visible) {
        this.successViewVisible = visible;
    }

    /**
     * @deprecated Use {@link #onReloadEvent(Context context, View view)} instead.
     */
    protected boolean onRetry(Context context, View view) {
        return false;
    }

    protected boolean onReloadEvent(Context context, View view) {
        return false;
    }

    public GammaCallback copy() {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        Object obj = null;
        try {
            oos = new ObjectOutputStream(bao);
            oos.writeObject(this);
            oos.close();
            ByteArrayInputStream bis = new ByteArrayInputStream(bao.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (GammaCallback) obj;
    }

    /**
     * @since 1.0.0.1
     */
    public View obtainRootView() {
        if (rootView == null) {
            rootView = View.inflate(context, onCreateView(), null);
        }
        return rootView;
    }

    public interface OnReloadListener extends Serializable {
        void onReload(View v);
    }

    protected abstract int onCreateView();

    /**
     * Called immediately after {@link #onCreateView()}
     *
     * @since 1.0.0.1
     */
    protected void onViewCreate(Context context, View view) {
    }

    /**
     * Called when the rootView of GammaCallback is attached to its LoadLayout.
     * @since 1.0.0.1
     */
    public void onAttach(Context context, View view) {
        onAttach(context, view,null);
    }

    /**
     *
     * @param context
     * @param view
     * @param bundle
     * @since 1.0.0.2
     */
    public void onAttach(Context context, View view, Bundle bundle) {

    }

    /**
     * Called when the rootView of GammaCallback is removed from its LoadLayout.
     *
     * @since 1.0.0.1
     */
    public void onDetach() {
    }

}
