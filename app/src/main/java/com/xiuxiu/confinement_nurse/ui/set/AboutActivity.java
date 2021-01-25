package com.xiuxiu.confinement_nurse.ui.set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.databinding.ActivityAboutBinding;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.MainActivity;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;
import com.xiuxiu.confinement_nurse.ui.login.HtmlActivity;
import com.xiuxiu.confinement_nurse.ui.set.vm.AboutBean;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class AboutActivity extends AbsBaseActivity {

    private ActivityAboutBinding inflate;

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected View getLayoutView() {
        inflate = ActivityAboutBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRequestPageSuccess();

        RxHttp.get(Configuration.KEY_CONTACT_US)
                .asXXResponse(AboutBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showLoadingDialog())
                .doFinally(() -> cancelLoadingDialog())
                .doOnError(throwable -> cancelLoadingDialog())
                .as(RxLife.as(this))
                .subscribe(s -> {
                    loadData(s);
                }, throwable -> {
                    ToastHelper.showToast("获取信息失败");
                });


        setListener();
    }

    private void setListener() {
        inflate.textPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HtmlActivity.start(v.getContext(),"http://ysxy.xiaoxiuapp.com");
            }
        });
        inflate.textUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HtmlActivity.start(v.getContext(),"http://ysxy.xiaoxiuapp.com");
            }
        });
    }

    private void loadData(AboutBean s) {
        AboutBean.DataBean item = s.getItem();
        if (item==null) {
            return;
        }
        if (TextUtils.isEmpty(item.getEmail())) {
            ViewHelper.hideView(inflate.mailboxContent);
        }else{
            inflate.mailbox.setText(item.getEmail());
        }

        if (TextUtils.isEmpty(item.getEmail())) {
            ViewHelper.hideView(inflate.phoneContent);
        }else{
            inflate.phone.setText(item.getPhone());
        }


        if (TextUtils.isEmpty(item.getWx())) {
            ViewHelper.hideView(inflate.wxContent);
        }else{
            inflate.wx.setText(item.getWx());
        }

        if (TextUtils.isEmpty(item.getQq())) {
            ViewHelper.hideView(inflate.qqContent);
        }else{
            inflate.qq.setText(item.getQq());
        }
    }
}
