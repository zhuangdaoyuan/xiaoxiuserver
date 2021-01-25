package com.xiuxiu.confinement_nurse.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.databinding.ActivityRecoverPasswordsBinding;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.login.view.XEditView;

/**
 * 找回密码
 */
public class RecoverPasswordActivity extends AbsBaseActivity implements XEditView.addTextChangedListener, PhoneNumberLoginContract.IView {

    private ActivityRecoverPasswordsBinding inflate;
    private String phone;
    private String code;
    private int type;

    public static void start(Context context, String phone, String code) {
        Intent starter = new Intent(context, RecoverPasswordActivity.class);
        starter.putExtra("phone", phone);
        starter.putExtra("type", 0);
        starter.putExtra("code", code);
        context.startActivity(starter);
    }

    public static void start(Context context, String phone) {
        Intent starter = new Intent(context, RecoverPasswordActivity.class);
        starter.putExtra("phone", phone);
        starter.putExtra("type", 1);
        context.startActivity(starter);
    }

    private PhoneNumberLoginContract.IPresenter presenter;

    @Override
    protected View getLayoutView() {
        inflate = ActivityRecoverPasswordsBinding.inflate(LayoutInflater.from(this));
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
        inflate.xdLoginPassword.setAddTextChangedListener(this);
        inflate.xdLoginNewPassword.setAddTextChangedListener(this);

        inflate.xdLoginPassword.setOnImageClickListener(new XEditView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                TransformationMethod transformationMethod = inflate.xdLoginPassword.getEditText().getTransformationMethod();
                if (transformationMethod instanceof PasswordTransformationMethod) {
                    inflate.xdLoginPassword.getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    inflate.xdLoginPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        inflate.xdLoginNewPassword.setOnImageClickListener(new XEditView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                TransformationMethod transformationMethod = inflate.xdLoginNewPassword.getEditText().getTransformationMethod();
                if (transformationMethod instanceof PasswordTransformationMethod) {
                    inflate.xdLoginNewPassword.getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    inflate.xdLoginNewPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void initViewState() {
        presenter = new PhoneNumberLoginPresenter(this);

        inflate.xdLoginPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inflate.xdLoginPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());

        inflate.xdLoginNewPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        if (type == 1) {
            inflate.xdLoginPassword.setTextHint("请输入旧密码");
            inflate.xdLoginNewPassword.setTextHint("请输入新密码");
        }

    }

    private void initView() {
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        type = getIntent().getIntExtra("type", 0);
    }

    @Override
    public void onTextChange() {
        String phone = inflate.xdLoginNewPassword.getTitle();
        String password = inflate.xdLoginPassword.getTitle();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(phone)) {
            inflate.btLogin.setEnabled(false);
        } else {
            inflate.btLogin.setEnabled(true);
        }
    }

    public void onDetermine(View view) {
        String newPassword = inflate.xdLoginPassword.getTitle();
        String password = inflate.xdLoginNewPassword.getTitle();

        if (type == 0) {
            presenter.requestResetPasswordByPassword(phone, code, newPassword, password);
        } else if (type == 1) {
            presenter.requestResetPasswordByPassword(newPassword, password);
        }
    }


    @Override
    public void onRequestPostCodeNum(long l) {

    }

    @Override
    public void onRequestPostCodeNumComplete() {

    }

    @Override
    public void onRequestRegisterSuccess() {

    }

    @Override
    public void onRequestNext(String phone, String code) {

    }

    @Override
    public void onRequestResetPasswordSuccess() {
        if (type == 0) {
            LoginActivity.start(this);
        }
        finish();
    }

    @Override
    public void onRequestChangeSuccess() {

    }
}
