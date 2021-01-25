package com.xiuxiu.confinement_nurse.ui.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.monster.gamma.callback.GammaCallback;
import com.monster.gamma.callback.LayoutEmpty;
import com.monster.gamma.callback.LayoutError;
import com.monster.gamma.callback.LayoutLoading;
import com.monster.gamma.core.Gamma;
import com.monster.gamma.core.LoadService;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;

public abstract class AbsBaseActivity extends BaseActivity implements PageStateViewer, LoadViewer, GammaCallback.OnReloadListener {
    protected abstract View getLayoutView();

    private LoadService loadService;
    private BasePopupView basePopupView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layoutView = getLayoutView();
        setContentView(layoutView);
        Object targetView = getTargetView();
        loadService = Gamma.getDefault().register(targetView == null ? this : targetView, this);
    }

    protected Object getTargetView() {
        return null;
    }

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
        finish();
    }

    @Override
    public void showLoadingDialog() {
        basePopupView=   new XPopup.Builder(this)
                .asLoading("正在加载中")
                .show();
    }

    @Override
    public void cancelLoadingDialog() {
        if (basePopupView!=null&&basePopupView.isShow()) {
            basePopupView.dismiss();
        }
    }

    @Override
    public void onReload(View v) {

    }

    public void onBack(View view) {
        finish();
    }
}
