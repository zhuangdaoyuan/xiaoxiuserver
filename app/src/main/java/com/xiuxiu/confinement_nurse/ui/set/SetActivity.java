package com.xiuxiu.confinement_nurse.ui.set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.xiuxiu.confinement_nurse.databinding.ActivitySetBinding;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;
import com.xiuxiu.confinement_nurse.ui.dialog.ConfirmDialog;
import com.xiuxiu.confinement_nurse.ui.login.FollowUpActivity;
import com.xiuxiu.confinement_nurse.ui.login.RecoverPasswordActivity;


public class SetActivity extends BaseActivity {

    private ActivitySetBinding inflate;

    public static void start(Context context) {
        Intent starter = new Intent(context, SetActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = ActivitySetBinding.inflate(LayoutInflater.from(this));
        setContentView(inflate.getRoot());
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void loadData() {
    }

    private void setListener() {
        inflate.view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.start(v.getContext());
            }
        });
        inflate.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone = ModelManager.getInstance().getCacheInterface().getUserPhone();
                if (!TextUtils.isEmpty(userPhone)) {
                    RecoverPasswordActivity.start(v.getContext(), userPhone);
                }
            }
        });
        inflate.view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FollowUpActivity.start(v.getContext());
            }
        });
        inflate.view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog confirmDialog = new ConfirmDialog(v.getContext());
                confirmDialog.setTitleContent("退出登录");
                confirmDialog.setOnConfirmListener(new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        UserHelper.switchVisitor();
                        finish();
                    }
                });
                new XPopup.Builder(v.getContext()).asCustom(confirmDialog)
                        .show();
            }
        });
    }

    private void initViewState() {
    }

    private void initView() {
    }

    public void onBack(View view) {
        finish();
    }
}
