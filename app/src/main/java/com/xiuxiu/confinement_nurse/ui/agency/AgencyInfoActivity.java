package com.xiuxiu.confinement_nurse.ui.agency;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityAgencyInfoBinding;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.AgencyInfoBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.login.AgencyRegisterActivity;
import com.xiuxiu.confinement_nurse.ui.user.UserInfoPresenter;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;

public class AgencyInfoActivity extends AbsBaseActivity implements AgencyInfoContract.IView {
    private static final int IMAGE_CODE = 1;
    private static final int IMAGE_CODE2 = 2;
    private ActivityAgencyInfoBinding inflate;
    private AgencyInfoPresenter presenter;
    private UserInfoPresenter userInfoPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, AgencyInfoActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected View getLayoutView() {
        inflate = ActivityAgencyInfoBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!UserHelper.isLogin()) {
            finish();
            return;
        }
        presenter = new AgencyInfoPresenter(this);

        userInfoPresenter = new UserInfoPresenter(this);
        userInfoPresenter.requestAgencyInfo(new XFunc0() {
            @Override
            public void call() {
                onRequestPageSuccess();
                UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                AgencyInfoBean agencyInfoBean = userBean.getAgencyInfoBean();
                presenter.setData(agencyInfoBean);
                loadUserInfo(agencyInfoBean);
            }
        }, new XFunc0() {
            @Override
            public void call() {
                onRequestPageError(0);
            }
        });
        setListener();


    }

    private void setListener() {
        inflate.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestSub();
            }
        });
        inflate.tvActivityMyInfoReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Matisse
//                        .from(AgencyInfoActivity.this)
//                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
//                        .countable(true)
//                        .maxSelectable(1)
////                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                        .spanCount(4)
//                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                        .thumbnailScale(0.85f)
//                        .imageEngine(new GlideEngine())
//                        .forResult(IMAGE_CODE);
            }
        });
    }

    private void loadUserInfo(AgencyInfoBean agencyInfoBean) {
        inflate.xfMyInfoName.setTitleRight1(agencyInfoBean.getTitle());
        inflate.xfMyInfoSex.setTitleRight1(agencyInfoBean.getContact());
        inflate.xfMyInfoAge.setTitleRight1(agencyInfoBean.getMobile());
        inflate.xfMyInfoResidentialAddress.setTitleRight1(agencyInfoBean.getDes());
        GlideHelper.loadImage(agencyInfoBean.getForeImg(), inflate.activityAgencyRegisterImage1);
        GlideHelper.loadImage(agencyInfoBean.getTailImg(), inflate.activityAgencyRegisterImage2);

        String orgImg = agencyInfoBean.getOrgImg();
        if (TextUtils.isEmpty(orgImg)) {
            orgImg=agencyInfoBean.getForeImg();
        }
        if (TextUtils.isEmpty(orgImg)) {
            orgImg=agencyInfoBean.getTailImg();
        }
        Glide.with(inflate.image1).load(orgImg)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.color.color_backdrop_placeholder)
                .error(R.color.color_backdrop_placeholder)
                .into(inflate.image1);

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
            presenter.requestUploadFile(mPaths, uris);
            String url = CollectionUtil.getSafe(mPaths, 0, "");
            Glide.with(inflate.image1).load(url)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .placeholder(R.color.color_backdrop_placeholder)
                    .error(R.color.color_backdrop_placeholder)
                    .into(inflate.image1);
        } else if (requestCode == IMAGE_CODE2 && RESULT_OK == resultCode) {
            if (data == null) {
                return;
            }
            List<Uri> uris = Matisse.obtainResult(data);
            List<String> mPaths = Matisse.obtainPathResult(data);
            presenter.requestUploadFile2(mPaths, uris);

            Uri safe = CollectionUtil.getSafe(uris, 0, null);
            Uri safe2 = CollectionUtil.getSafe(uris, 1, null);

            GlideHelper.loadUriImage(safe, ResHelper.pt2Px(20), inflate.activityAgencyRegisterImage1);
            GlideHelper.loadUriImage(safe2, ResHelper.pt2Px(20), inflate.activityAgencyRegisterImage2);
        }
    }

    public void onUpdateName(View view) {
//        DialogHelper.showInputDialog(this, "修改名称", new XFunc1<String>() {
//            @Override
//            public void call(String title) {
//                presenter.rquestName(title);
//                inflate.xfMyInfoName.setTitleRight1(TextUtils.isEmpty(title) ? "未填写" : title);
//            }
//        });
    }

    public void onUpdateContactPerson(View view) {
//        DialogHelper.showInputDialog(this, "修改联系人", new XFunc1<String>() {
//            @Override
//            public void call(String title) {
//                presenter.onUpdateContactPerson(title);
//                inflate.xfMyInfoName.setTitleRight1(TextUtils.isEmpty(title) ? "未填写" : title);
//            }
//        });
    }

    public void onUpdatContactNumber(View view) {
//        DialogHelper.showInputDialog(this, "修改联系电话", new XFunc1<String>() {
//            @Override
//            public void call(String title) {
//                presenter.onUpdatContactNumber(title);
//                inflate.xfMyInfoName.setTitleRight1(TextUtils.isEmpty(title) ? "未填写" : title);
//            }
//        });
    }

    public void onUpdateSelfIntroduction(View view) {
//        DialogHelper.showInputDialog(this, "机构自我介绍", new XFunc1<String>() {
//            @Override
//            public void call(String title) {
//                presenter.onUpdateSelfIntroduction(title);
//                inflate.xfMyInfoName.setTitleRight1(TextUtils.isEmpty(title) ? "未填写" : title);
//            }
//        });
    }

    public void onClickUpdateImage(View view) {
//        Matisse
//                .from(AgencyInfoActivity.this)
//                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
//                .countable(false)
//                .maxSelectable(2)
////                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .spanCount(4)
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .thumbnailScale(0.85f)
//                .imageEngine(new GlideEngine())
//                .forResult(IMAGE_CODE2);
    }
}
