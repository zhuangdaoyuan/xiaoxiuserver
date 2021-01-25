package com.xiuxiu.confinement_nurse.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.databinding.ActivityResetPasswordBinding;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.login.view.XEditCodeView;
import com.xiuxiu.confinement_nurse.ui.login.view.XEditView;

/**
 *
 */
public class ResetPasswordActivity extends AbsBaseActivity implements PhoneNumberLoginContract.IView, XEditView.addTextChangedListener, XEditCodeView.addTextChangedListener {


    private ActivityResetPasswordBinding inflate;
    private PhoneNumberLoginContract.IPresenter presente;

    public static void start(Context context) {
        Intent starter = new Intent(context, ResetPasswordActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected View getLayoutView() {
        inflate = ActivityResetPasswordBinding.inflate(LayoutInflater.from(this));
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
        onTextChange();
    }

    private void loadData() {
    }

    private void setListener() {
        inflate.xdLoginCode.setOnImageClickListener(new XEditCodeView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                onPostCode();
            }
        });
        inflate.xdLoginPhone.setOnImageClickListener(new XEditView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                inflate.xdLoginPhone.setText("");
            }
        });
        inflate.xdLoginPhone.setAddTextChangedListener(this);
        inflate.xdLoginCode.setAddTextChangedListener(this);
    }

    private void initViewState() {
        presente = new PhoneNumberLoginPresenter(this);
        inflate.xdLoginCode.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        inflate.xdLoginPhone.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        inflate.xdLoginCode.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
    }

    private void initView() {
    }

    public void onNext(View view) {
        String phone = inflate.xdLoginPhone.getTitle();
        String code = inflate.xdLoginCode.getTitle();
        presente.requestResetPasswordCheck(phone,code);
    }

    private void onPostCode() {
        String phone = inflate.xdLoginPhone.getTitle();
        presente.requestResetPasswordCode(phone);
    }


    @Override
    public void onRequestPostCodeNum(long l) {
        if (!inflate.xdLoginCode.isEnabled()) {
            inflate.xdLoginCode.setEnabled(false);
        }
        inflate.xdLoginCode.setTextRight("重获" + l + "s");
    }

    @Override
    public void onRequestPostCodeNumComplete() {
        inflate.xdLoginCode.setTextRight("获取验证码");
        inflate.xdLoginCode.setEnabled(true);
    }

    @Override
    public void onRequestRegisterSuccess() {

    }

    @Override
    public void onRequestNext(String phone,String code) {
        RecoverPasswordActivity.start(this, phone,code);
    }

    @Override
    public void onRequestResetPasswordSuccess() {

    }

    @Override
    public void onRequestChangeSuccess() {


    }

    @Override
    public void onTextChange() {
        String phone = inflate.xdLoginPhone.getTitle();
        String password = inflate.xdLoginCode.getTitle();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(phone)) {
            inflate.btLogin.setEnabled(false);
        } else {
            inflate.btLogin.setEnabled(true);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
