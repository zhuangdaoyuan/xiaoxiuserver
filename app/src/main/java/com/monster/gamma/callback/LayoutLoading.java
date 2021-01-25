package com.monster.gamma.callback;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.utlis.AdaptScreenUtils;


public class LayoutLoading extends GammaCallback {


    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }

    @Override
    protected void onViewCreate(Context context, View view) {
        super.onViewCreate(context, view);
    }

    @Override
    public void onAttach(Context context, View view, Bundle bundle) {
        super.onAttach(context, view, bundle);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
