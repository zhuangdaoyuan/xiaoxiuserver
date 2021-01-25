package com.xiuxiu.confinement_nurse.ui.my;

import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;

public abstract class ExperiencePresenter<T extends LoadViewer> extends BasePresenter<T > {

    public ExperiencePresenter(T viewer) {
        super(viewer);
    }



    protected abstract void onRequestFinish();
}
