package com.xiuxiu.confinement_nurse.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityUserAgreementBinding;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

/**
 * 用户协议
 */
public class UserAgreementActivity extends AbsBaseActivity {

    private  ActivityUserAgreementBinding inflate;

    public static void start(Context context) {
        Intent starter = new Intent(context, UserAgreementActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected View getLayoutView() {
        inflate = ActivityUserAgreementBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRequestPageSuccess();
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
