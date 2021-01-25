package com.monster.gamma.callback;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.xiuxiu.confinement_nurse.R;


public class LayoutError extends GammaCallback {


    @Override
    protected int onCreateView() {
        return R.layout.layout_error;
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
