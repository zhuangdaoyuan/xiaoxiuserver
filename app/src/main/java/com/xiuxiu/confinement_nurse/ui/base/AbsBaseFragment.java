package com.xiuxiu.confinement_nurse.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.monster.gamma.callback.GammaCallback;
import com.monster.gamma.callback.LayoutEmpty;
import com.monster.gamma.callback.LayoutError;
import com.monster.gamma.callback.LayoutLoading;
import com.monster.gamma.core.Gamma;
import com.monster.gamma.core.LoadService;
import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;


public abstract class AbsBaseFragment extends BaseFragment implements PageStateViewer, LoadViewer, GammaCallback.OnReloadListener {
    private int direction = -1;

    protected LoadService loadService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutView(inflater, container);
        Object targetView = getTargetView();
        if (targetView == null) {
            targetView = view;
        }
        loadService = Gamma.getDefault().register(targetView, this);
        return loadService.getLoadLayout();

    }

    protected Object getTargetView() {
        return null;
    }


    protected abstract View getLayoutView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    @Override
    public void onRequestPageError(int code) {
        loadService.showCallback(LayoutError.class);
    }

    @Override
    public void onRequestPageNetError() {
        loadService.showCallback(LayoutError.class);
    }

    @Override
    public void onRequestPageSuccess() {
        loadService.showSuccess();
    }

    @Override
    public void onRequestPageEmpty() {
        loadService.showCallback(LayoutEmpty.class);
    }

    @Override
    public void onRequestLoading() {
        loadService.showCallback(LayoutLoading.class);
    }

    @Override
    public void onRequestFinish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }


    private BasePopupView basePopupView;

    @Override
    public void showLoadingDialog() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    basePopupView = new XPopup.Builder(getContext())
                            .asLoading("正在加载中")
                            .show();
                }
            });
        }
    }

    @Override
    public void cancelLoadingDialog() {
        basePopupView.post(new Runnable() {
            @Override
            public void run() {
                if (basePopupView != null) {
                    basePopupView.dismiss();
                }
            }
        });

    }

    @Override
    public void onReload(View v) {

    }
}
