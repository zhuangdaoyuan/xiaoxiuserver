package com.xiuxiu.confinement_nurse.ui.area;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityLocationBinding;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;

import org.jetbrains.annotations.NotNull;


public class LocationActivity extends AbsBaseActivity {

    private  ActivityLocationBinding inflate;

    public static void start(Context context) {
        Intent starter = new Intent(context, LocationActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected View getLayoutView() {
        inflate = ActivityLocationBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void loadData() {
    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }
}
