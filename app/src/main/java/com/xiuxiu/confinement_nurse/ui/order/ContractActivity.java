package com.xiuxiu.confinement_nurse.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.databinding.ActivityContractBinding;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;
import com.xiuxiu.confinement_nurse.ui.order.vm.ContractVm;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;


/**
 * 合同
 */
public class ContractActivity extends AbsBaseActivity {

    private String orderId;
    private String matronId;
    public static void start(Context context, String orderId) {
        start(context,orderId,"");
    }
    public static void start(Context context, String orderId,String matronId) {
        Intent starter = new Intent(context, ContractActivity.class);
        starter.putExtra("orderId", orderId);
        starter.putExtra("matronId",matronId);
        context.startActivity(starter);
    }
    private ActivityContractBinding inflate;

    @Override
    protected View getLayoutView() {
        inflate = ActivityContractBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected Object getTargetView() {
        return super.getTargetView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initViewState(savedInstanceState);
        setListener();
        loadData(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("orderId", orderId);
        outState.putString("matronId", matronId);
    }

    private void loadData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            orderId = savedInstanceState.getString("orderId");
            matronId=savedInstanceState.getString("matronId");
        } else {
            orderId = getIntent().getStringExtra("orderId");
            matronId=getIntent().getStringExtra("matronId");
        }
        if (TextUtils.isEmpty(matronId)) {
            RxHttp.get(Configuration.Order.KEY_ORDER_CONTRACT)
                    .add("orderId", orderId)
                    .asXXResponse(ContractVm.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxLife.as(this))
                    .subscribe(s -> {
                        onRequestPageSuccess();
                        loadContractInfo(s.getItem());
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        }
                        onRequestPageError(0);
                    });
        }else{
            RxHttp.get(Configuration.mechanism.KEY_ORDER_CONTRACT)
                    .add("orderId", orderId)
                    .add("ysId", matronId)
                    .asXXResponse(ContractVm.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxLife.as(this))
                    .subscribe(s -> {
                        onRequestPageSuccess();
                        loadContractInfo(s.getItem());
                    }, throwable -> {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        }
                        onRequestPageError(0);
                    });
        }

    }

    private void loadContractInfo(ContractVm.ContractDetailVm s) {
        inflate.text1.setText(s.getMatronName());
        inflate.text2.setText(s.getMatronIdNo());
        inflate.text3.setText(s.getUserName());
        inflate.text4.setText(s.getUserIdNo());
    }

    private void setListener() {
        inflate.btActivityFilterDefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = inflate.agree.isChecked();
                if (!selected) {
                    ToastHelper.showToast("您还未勾选同意");
                    return;
                }

                finish();
            }
        });
        inflate.agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                inflate.btActivityFilterDefine.setEnabled(isChecked);
            }
        });

        inflate.btActivityFilterDefine.setEnabled(inflate.agree.isChecked());
    }

    private void initViewState(Bundle savedInstanceState) {

    }

    private void initView() {
    }
}
