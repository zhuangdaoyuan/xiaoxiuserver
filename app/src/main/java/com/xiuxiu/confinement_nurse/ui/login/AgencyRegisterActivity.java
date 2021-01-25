package com.xiuxiu.confinement_nurse.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.databinding.ActivityAgencyRegistersBinding;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.db.pojo.AgencyInfoBean;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.login.view.XEditCodeView;
import com.xiuxiu.confinement_nurse.ui.login.view.XEditView;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;

import io.rong.callkit.util.GlideUtils;

public class AgencyRegisterActivity extends AbsBaseActivity implements PhoneNumberLoginContract.IView, AgencyRegisterContract.IView {
    private PhoneNumberLoginPresenter phoneNumberLoginPresenter;
    private ActivityAgencyRegistersBinding inflate;
    private static final int IMAGE_CODE = 1;

    private AgencyRegisterContract.IPresenter mPresnter;
    public static void start(Context context) {
        Intent starter = new Intent(context, AgencyRegisterActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected View getLayoutView() {
        inflate = ActivityAgencyRegistersBinding.inflate(LayoutInflater.from(this));
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
        inflate.activityAgencyRegisterCode.setOnImageClickListener(new XEditCodeView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                postCode();
            }
        });
        inflate.activityAgencyRegisterPhone.setOnImageClickListener(new XEditView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                inflate.activityAgencyRegisterPhone.setText("");
            }
        });
        inflate.activityAgencyRegisterName.setOnImageClickListener(new XEditView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                inflate.activityAgencyRegisterName.setText("");
            }
        });
        inflate.activityAgencyRegisterContactPerson.setOnImageClickListener(new XEditView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                inflate.activityAgencyRegisterContactPerson.setText("");
            }
        });
        inflate.activityAgencyRegisterTelephone.setOnImageClickListener(new XEditView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                inflate.activityAgencyRegisterTelephone.setText("");
            }
        });
        inflate.xdLoginPassword.setOnImageClickListener(new XEditView.OnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                inflate.xdLoginPassword.setText("");
            }
        });
        inflate.activityAgencyRegisterImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse
                        .from(AgencyRegisterActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .countable(false)
                        .maxSelectable(2)
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .spanCount(4)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(IMAGE_CODE);
            }
        });
        inflate.activityAgencyRegisterImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse
                        .from(AgencyRegisterActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .countable(false)
                        .maxSelectable(2)
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .spanCount(4)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(IMAGE_CODE);
            }
        });
    }

    private void initView() {
        mPresnter = new AgencyRegisterPresenter(this);
        phoneNumberLoginPresenter = new PhoneNumberLoginPresenter(this);
    }

    private void initViewState() {
        inflate.activityAgencyRegisterPhone.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        inflate.activityAgencyRegisterTelephone.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        inflate.ivProtocol.setSelected(true);
    }

    public void postCode() {
        boolean selected = inflate.ivProtocol.isSelected();
        String str = inflate.activityAgencyRegisterPhone.getTitle().toString();
        if (!selected) {
            ToastHelper.showToast("请同意协议");
            return;
        }
        phoneNumberLoginPresenter.requestPostCode(str,4);
    }

    public void onNext(View view) {
        boolean selected = inflate.ivProtocol.isSelected();
        if (!selected) {
            ToastHelper.showToast("请同意协议");
            return;
        }
        String phone = inflate.activityAgencyRegisterPhone.getTitle();
        String code = inflate.activityAgencyRegisterCode.getTitle();
        String name = inflate.activityAgencyRegisterName.getTitle();
        String person = inflate.activityAgencyRegisterContactPerson.getTitle();
        String title1 = inflate.activityAgencyRegisterTelephone.getTitle();
        String s = inflate.activityAgencyRegisterMsgTitle.getText().toString();
        String password = inflate.xdLoginPassword.getTitle();
        mPresnter.requestSub(phone, code, password, name, person, title1, s);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CODE && RESULT_OK == resultCode) {
            if (data == null) {
                return;
            }
            List<Uri> uris = Matisse.obtainResult(data);
            List<String> mPaths = Matisse.obtainPathResult(data);
            mPresnter.requestUploadFile(mPaths, uris);

            Uri safe = CollectionUtil.getSafe(uris, 0, null);
            Uri safe2 = CollectionUtil.getSafe(uris, 1, null);

            GlideHelper.loadUriImage(safe, ResHelper.pt2Px(20), inflate.activityAgencyRegisterImage1);
            GlideHelper.loadUriImage(safe2, ResHelper.pt2Px(20), inflate.activityAgencyRegisterImage2);
        }
    }

    public void onClickUserAgreement(View view) {
        UserAgreementActivity.start(view.getContext());
    }

    @Override
    public void onRequestPostCodeNum(long l) {

    }

    @Override
    public void onRequestPostCodeNumComplete() {

    }

    @Override
    public void onRequestRegisterSuccess() {
        finish();
    }

    @Override
    public void onRequestAgencyInfo(AgencyInfoBean loginBean) {

    }

    @Override
    public void onRequestNext(String phone, String code) {

    }

    @Override
    public void onRequestResetPasswordSuccess() {

    }

    @Override
    public void onRequestChangeSuccess() {

    }
}
