package com.xiuxiu.confinement_nurse.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.databinding.ActivityLoginPhomeNumberBinding;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.login.view.XEditCodeView;
import com.xiuxiu.confinement_nurse.ui.login.view.XEditView;

/**
 * 手机号码快速注册
 */
public class PhoneNumberLoginActivity extends AbsBaseActivity implements XEditView.addTextChangedListener, PhoneNumberLoginContract.IView, XEditCodeView.addTextChangedListener {

    private ActivityLoginPhomeNumberBinding inflate;
    private PhoneNumberLoginContract.IPresenter presente;

    public static void start(Context context) {
        Intent starter = new Intent(context, PhoneNumberLoginActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected View getLayoutView() {
        inflate = ActivityLoginPhomeNumberBinding.inflate(LayoutInflater.from(this));
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
        inflate.tvProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = inflate.ivProtocol.isSelected();
                inflate.ivProtocol.setSelected(!selected);
            }
        });
        inflate.ivProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = inflate.ivProtocol.isSelected();
                inflate.ivProtocol.setSelected(!selected);
            }
        });
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
        inflate.xdLoginPhone.setAddTextChangedListener(this);
        inflate.xdLoginPassword.setAddTextChangedListener(this);
        inflate.xdLoginCode.setAddTextChangedListener(this);

        inflate.userAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HtmlActivity.start(v.getContext(),"http://ysxy.xiaoxiuapp.com/ruzhu.html");
                HtmlActivity.start(v.getContext(),"http://ysxy.xiaoxiuapp.com");
            }
        });
    }


    private void initViewState() {
        presente = new PhoneNumberLoginPresenter(this);
        inflate.xdLoginCode.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        inflate.xdLoginPhone.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        inflate.xdLoginPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inflate.xdLoginPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        inflate.ivProtocol.setSelected(true);
    }

    private void initView() {
    }

    @Override
    public void onTextChange() {
        String phone = inflate.xdLoginPhone.getTitle();
        String password = inflate.xdLoginPassword.getTitle();
        String code = inflate.xdLoginCode.getTitle();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(code)) {
            inflate.btLogin.setEnabled(false);
        } else {
            inflate.btLogin.setEnabled(true);
        }
    }

    private void onPostCode() {
        boolean selected = inflate.ivProtocol.isSelected();
        if (!selected) {
            ToastHelper.showToast("请同意协议");
            return;
        }
        String phone = inflate.xdLoginPhone.getTitle();
        presente.requestPostCode(phone);
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
        finish();
    }

    @Override
    public void onRequestNext(String phone,String code) {

    }

    @Override
    public void onRequestResetPasswordSuccess() {

    }

    @Override
    public void onRequestChangeSuccess() {

    }

    public void onRegister(View view) {

        boolean selected = inflate.ivProtocol.isSelected();
        if (!selected) {
            ToastHelper.showToast("请同意协议");
            return;
        }
        String phone = inflate.xdLoginPhone.getTitle();
        String password = inflate.xdLoginPassword.getTitle();
        String code = inflate.xdLoginCode.getTitle();
        presente.requestRegister(phone, password, code);
    }

    public void onClickUserAgreement(View view) {
        UserAgreementActivity.start(view.getContext());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
