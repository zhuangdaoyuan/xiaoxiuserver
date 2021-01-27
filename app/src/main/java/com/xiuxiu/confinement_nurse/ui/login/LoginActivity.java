package com.xiuxiu.confinement_nurse.ui.login;

import android.app.Activity;
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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.monster.share.SocialHelper;
import com.monster.share.callback.SocialLoginCallback;
import com.monster.share.entities.ThirdInfoEntity;
import com.xiuxiu.confinement_nurse.Constants;
import com.xiuxiu.confinement_nurse.databinding.ActivityLoginBinding;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.login.view.XEditView;
import com.xiuxiu.confinement_nurse.ui.user.UserInfoContract;
import com.xiuxiu.confinement_nurse.ui.user.UserInfoPresenter;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_LOGIN_SUCCESS;

/**
 * 登录
 */
@Route(path = RouterHelper.KEY_LOGIN)
public class LoginActivity extends AbsBaseActivity implements XEditView.addTextChangedListener, LoginContract.IView {

    private ActivityLoginBinding inflate;

    public static void start(Activity context) {
//        ARouter.getInstance().build(RouterHelper.KEY_LOGIN).greenChannel().navigation();
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivityForResult(intent,1);
    }

    LoginContract.IPresenter loginPresenter;
    UserInfoContract.IPresenter userPresenter;

    @Override
    protected View getLayoutView() {
        inflate = ActivityLoginBinding.inflate(LayoutInflater.from(this));
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ViewHelper.showView(inflate.layoutType);
    }

    private void loadData() {

    }

    private void setListener() {
        inflate.layoutUserTypeYuesao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelManager.getInstance().getCacheInterface().saveUserType(2);
                ViewHelper.hideView(inflate.layoutType);
            }
        });
        inflate.layoutUserTypeMechanism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelManager.getInstance().getCacheInterface().saveUserType(4);
                ViewHelper.hideView(inflate.layoutType);
            }
        });
        inflate.activityLoginAgency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgencyRegisterActivity.start(v.getContext());
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
        inflate.btLogin.setOnClickListener(v -> login());


    }


    private void initViewState() {
        userPresenter = new UserInfoPresenter(this);
        loginPresenter = new LoginPresenter(this);

        inflate.xdLoginPhone.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);

        inflate.xdLoginPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inflate.xdLoginPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    private void initView() {
    }

    @Override
    public void onTextChange() {
        String phone = inflate.xdLoginPhone.getTitle();
        String password = inflate.xdLoginPassword.getTitle();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(phone)) {
            inflate.btLogin.setEnabled(false);
        } else {
            inflate.btLogin.setEnabled(true);
        }
    }

    private void login() {

        String phone = inflate.xdLoginPhone.getTitle();
        String password = inflate.xdLoginPassword.getTitle();

        loginPresenter.requestLogin(phone, password, TextUtils.equals(String.valueOf(ModelManager.getInstance().getCacheInterface().getUserType()), Constants.KEY_USE));
    }


    public void onFastRegister(View view) {
        if (TextUtils.equals(String.valueOf(ModelManager.getInstance().getCacheInterface().getUserType()), Constants.KEY_AGENCY)) {
            AgencyRegisterActivity.start(view.getContext());
            return;
        }
        PhoneNumberLoginActivity.start(view.getContext());
    }

    public void onForgetPassword(View view) {
        ResetPasswordActivity.start(view.getContext());
    }

    @Override
    public void onRequestLoginSuccess(String xtoken, String userId) {


        userPresenter.requestUserInfo(xtoken, userId, new XFunc0() {
            @Override
            public void call() {
                setResult(KEY_LOGIN_SUCCESS);
                finish();
            }
        }, null);
    }

    @Override
    public void onRequestAginVerify() {
        AgencyAgainRegisterActivity.start(this);
    }

    @Override
    public void onRequestAgencyLoginSuccess(String xtoken, String userId) {
        userPresenter.requestAgencyInfo(xtoken, userId, new XFunc0() {
            @Override
            public void call() {
                setResult(KEY_LOGIN_SUCCESS);
                finish();
            }
        }, null);
    }

    public void onClickWxLogin(View view) {
        new SocialHelper.Builder()
                .setWxAppId("")
                .setWxAppSecret("")
                .build().loginWX(this, new SocialLoginCallback() {
            @Override
            public void loginSuccess(ThirdInfoEntity info) {

            }

            @Override
            public void socialError(String msg) {

            }
        });
    }
}
