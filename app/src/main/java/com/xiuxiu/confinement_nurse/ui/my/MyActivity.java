package com.xiuxiu.confinement_nurse.ui.my;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.monster.share.SocialHelper;
import com.monster.share.callback.SocialShareCallback;
import com.monster.share.entities.ShareEntity;
import com.monster.share.entities.WXShareEntity;
import com.noober.background.drawable.DrawableCreator;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityMyBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.model.IntCodeEnum;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.CertificateInfoBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.model.event.ExperienceEvent;
import com.xiuxiu.confinement_nurse.model.event.UserInfoEvent;
import com.xiuxiu.confinement_nurse.ui.agency.user.AddYuesaoActivity;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.SpacesItemDecoration;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.OtherExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.SelfExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.ServiceExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.adapter.LearningExperienceAdapter;
import com.xiuxiu.confinement_nurse.ui.my.adapter.OtherExperienceAdapter;
import com.xiuxiu.confinement_nurse.ui.my.adapter.SelfExpressionAdapter;
import com.xiuxiu.confinement_nurse.ui.my.adapter.ServiceExperienceAdapter;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderServiceVm;
import com.xiuxiu.confinement_nurse.ui.user.UserInfoContract;
import com.xiuxiu.confinement_nurse.ui.user.UserInfoPresenter;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.SocialUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.monster.share.entities.ShareEntity.TYPE_WX;
import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_NEED_LOGIN;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_CERTIFIED;


/**
 * @author 11925
 */
@Route(path = RouterHelper.KEY_MY, extras = KEY_NEED_LOGIN)
public class MyActivity extends AbsBaseActivity implements MyContract.IView {
    private UserInfoBean userInfoBean;
    public static void start() {
       start(null);
    }

    public static void start(UserInfoBean ysId) {
        ARouter.getInstance().build(RouterHelper.KEY_MY)
                .withSerializable("ysid",ysId)
                .navigation();
    }


    private ActivityMyBinding inflate;
    private MyContract.IPresenter presenter;
    private LearningExperienceAdapter learningExperienceAdapter;
    private OtherExperienceAdapter otherExperienceAdapter;
    private ServiceExperienceAdapter serviceExperienceAdapter;
    private UserInfoContract.IPresenter userPresenter;


    private CertificateInfoBean.CertificateInfo certificateInfo1;
    private CertificateInfoBean.CertificateInfo certificateInfo2;
    private CertificateInfoBean.CertificateInfo certificateInfo3;
    private CertificateInfoBean.CertificateInfo certificateInfo4;
    private CertificateInfoBean.CertificateInfo certificateInfo5;
    private CertificateInfoBean.CertificateInfo certificateInfo6;

    private SelfExperienceVm selfExperienceVm;

    @Override
    protected View getLayoutView() {
        inflate = ActivityMyBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected Object getTargetView() {
        return inflate.activityMyBg;
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

    @Override
    protected int getStatusBarColor() {
        return R.color.color_F7D1FF_a100;
    }


    private void loadData() {
        presenter.requestLoadData();
    }


    private void setListener() {

        inflate.hpvActivityMy.setOnClickListener(v -> {
            if (userInfoBean==null) {
                MyInfoActivity.start();
            }else{
                AddYuesaoActivity.start(userInfoBean);
            }
        });

        EventBus.OtherExperience().observe(this, experienceEvent -> presenter.requestLoadOtherList());
        EventBus.LearningExperience().observe(this, experienceEvent -> presenter.requestLoadEducationList());
        EventBus.SelfEvaluation().observe(this, experienceEvent -> presenter.requestLoadSelfExperience());
    }

    private void initViewState() {

        //////学习经历///////////////////

        learningExperienceAdapter = new LearningExperienceAdapter();
        View view = LayoutInflater.from(this).inflate(R.layout.layout_learning_experience_header, null);
        learningExperienceAdapter.addHeaderView(view);
        inflate.rvActivityMyLearningExperience.setAdapter(learningExperienceAdapter);
        inflate.rvActivityMyLearningExperience.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        learningExperienceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Object safe = CollectionUtil.getSafe(adapter.getData(), position, null);
                if (safe instanceof LearningExperienceVm.LearningExperience) {
                    String type = ((LearningExperienceVm.LearningExperience) safe).getType();
                    if (TextUtils.equals(type, "1")) {
                        ServiceExperienceActivity.start(view.getContext(), presenter.getYsId(),(LearningExperienceVm.LearningExperience) safe);
                    } else if (TextUtils.equals(type, "2")) {
                        LearningExperienceActivity.start(view.getContext(),presenter.getYsId(), (LearningExperienceVm.LearningExperience) safe);
                    }
                }
            }
        });
        View learningExperienceEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_experience_empty, null);
        learningExperienceEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceExperienceActivity.start(v.getContext(), presenter.getYsId(),null);
            }
        });
        learningExperienceAdapter.setEmptyView(learningExperienceEmptyView);


        //////服务经历///////////////////////////////


        serviceExperienceAdapter = new ServiceExperienceAdapter();
        inflate.rvActivityMyServiceExperience.setAdapter(serviceExperienceAdapter);
        inflate.rvActivityMyServiceExperience.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        serviceExperienceAdapter.setEmptyView(R.layout.layout_experience_empty);

        serviceExperienceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                OrderServiceVm item = (OrderServiceVm) adapter.getItem(position);
                ServiceExperienceItemActivity.start(view.getContext(),item);
            }
        });

        ///////////其他经历///////////////////////////////

        otherExperienceAdapter = new OtherExperienceAdapter();
        inflate.rvActivityMyOtherExperience.setAdapter(otherExperienceAdapter);
        inflate.rvActivityMyOtherExperience.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        View view2 = LayoutInflater.from(this).inflate(R.layout.layout_experience_empty, null);
        otherExperienceAdapter.setEmptyView(view2);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherExperienceActivity.start(v.getContext(), presenter.getYsId(),null);
            }
        });
        otherExperienceAdapter.setOnItemClickListener((adapter, view1, position) -> {
            Object safe = CollectionUtil.getSafe(adapter.getData(), position, null);
            OtherExperienceActivity.start(view1.getContext(),presenter.getYsId(), (OtherExperienceVm.OtherExperience) safe);
        });
        ///////////自我展示////////////////////////////////////

    }

    private void initView() {
        presenter = new MyPresenter(this);
        userPresenter = new UserInfoPresenter(this);
        Serializable ysid = getIntent().getSerializableExtra("ysid");
        if(ysid instanceof UserInfoBean){
            userInfoBean= (UserInfoBean) ysid;
            String matronId = (userInfoBean.getMatronId());
            presenter.setYsId(matronId);
            loadUserBeanData( ((UserInfoBean) ysid));

//            EventBus.UpdateEvent().observe(this, loginEvent -> loadData());
        }else {
            loadUserBeanData(ModelManager.getInstance().getUserInterface().requestCurrentUserBean().getItem());
        }
//        userPresenter.requestUserInfoByCertificate(presenter.getYsId(),new XFunc1<Boolean>() {
//            @Override
//            public void call(Boolean aBoolean) {
//                loadUserBeanData(ModelManager.getInstance().getUserInterface().requestCurrentUserBean().getItem());
//            }
//        }, null);
    }


    public void onClickUpdateIdCard(View view) {
       if (certificateInfo1!=null&&TextUtils.equals(certificateInfo1.getState(), String.valueOf(KEY_CERTIFIED))) {
            ToastHelper.showToast("认证通过");
        }
        openImageView(1);
    }

    public void onClickUpdateFaceRecognition(View view) {
        if (certificateInfo2 == null || !TextUtils.equals(certificateInfo2.getState(), String.valueOf(KEY_CERTIFIED))) {
//            openImageView(2);
        } else if (TextUtils.equals(certificateInfo2.getState(), String.valueOf(KEY_CERTIFIED))) {
            ToastHelper.showToast("认证通过");
        }
        openImageView(2);
    }

    public void onClickUpdateHealthCertificate(View view) {
        if (certificateInfo3!=null&&TextUtils.equals(certificateInfo3.getState(), String.valueOf(KEY_CERTIFIED))) {
            ToastHelper.showToast("认证通过");
        }else{
        }
        openImageView(3);
    }

    public void onClickNoCriminalProof(View view) {
        if (certificateInfo4!=null&&TextUtils.equals(certificateInfo4.getState(), String.valueOf(KEY_CERTIFIED))) {
            ToastHelper.showToast("认证通过");
        }else{
        }
        openImageView(4);
    }

    //育婴师证
    public void onClickCardForMurse(View view) {
        if (certificateInfo6!=null&&TextUtils.equals(certificateInfo6.getState(), String.valueOf(KEY_CERTIFIED))) {
            ToastHelper.showToast("认证通过");
        }else{
        }
        openImageView(6);
    }

    //月嫂证
    public void onClickUpdateYuesao(View view) {
        if (certificateInfo5!=null&&TextUtils.equals(certificateInfo5.getState(), String.valueOf(KEY_CERTIFIED))) {
            ToastHelper.showToast("认证通过");
        }else{
        }
        openImageView(5);
    }


    public void onClickLookOver(View view) {
        ServiceExperienceActivity.start(view.getContext(), presenter.getYsId(),null);
    }

    private void openImageView(int code) {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(1)
                .spanCount(4)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(code);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (RESULT_OK == resultCode) {
            List<Uri> uris = Matisse.obtainResult(data);
            List<String> mPaths = Matisse.obtainPathResult(data);

            Uri safe = CollectionUtil.getSafe(uris, 0, null);
            String path = CollectionUtil.getSafe(mPaths, 0, null);

            presenter.requestUploadCertification(requestCode, safe, path, "");
        }
    }


    private void loadUserCertificateInfo(CertificateInfoBean userBean) {
        if (UserHelper.whetherItIsCertified(userBean, IntCodeEnum.KEY_MATRON_CERT)) {
            ViewHelper.showView(inflate.tvActivityMyTag1);
        } else {
            ViewHelper.hideView(inflate.tvActivityMyTag1);
        }
        if (UserHelper.whetherItIsCertified(userBean, IntCodeEnum.KEY_NURSEMAID_CERT)) {
            ViewHelper.showView(inflate.tvActivityMyTag2);
        } else {
            ViewHelper.hideView(inflate.tvActivityMyTag2);
        }
    }


    private void loadCertificateInfoBean(UserInfoBean item){
//        CertificateInfoBean certificateInfoBean = userBean.getCertificateInfoBean();
//        loadUserCertificateInfo(userBean);
//        loadUserCertificateInfoState(certificateInfoBean);

    }
    private void loadUserBeanData(UserInfoBean item) {
        if (item == null) {
            return;
        }
        inflate.vActivityMyUserName.setText(TextUtils.isEmpty(item.getUserName()) ? "未填写" :
                item.getUserName());
        inflate.vActivityMyArea.setText(item.getNativePlaceName());
        inflate.vActivityOrderDetailsUserType.setText(item.getAge() + "岁");
//        inflate.hpvActivityMy.loadData(item.getUserAvatar());
        Drawable build = new DrawableCreator.Builder().setSolidColor(ResHelper.getColor(R.color.color_backdrop_default)).setCornersRadius(ResHelper.pt2Px(23)).build();

        String avatarList = item.getAvatarList();
        if (!TextUtils.isEmpty(avatarList)) {
            String[] split = avatarList.split(",");
            Glide.with(inflate.hpvActivityMy).load(split[0]).placeholder(build)
                    .error(build).into(inflate.hpvActivityMy);
        }

        try {
            int age = item.getAge();
            if (age==0) {
                String s = DateHelper.BirthdayToAge(item.getBirthday());
                inflate.vActivityOrderDetailsUserType.setText(s+"岁");
            }else{
                inflate.vActivityOrderDetailsUserType.setText(age+"岁");
            }
        }catch (Exception e){

        }
    }

    @Override
    public void loadUserCertificateInfoState(CertificateInfoBean certificateInfoBean) {
        if (certificateInfoBean == null) {
            return;
        }
        loadUserCertificateInfo(certificateInfoBean);
        for (CertificateInfoBean.CertificateInfo c : certificateInfoBean.getItems()) {
            if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_ID_CARD)) {
                certificateInfo1 = c;
                if (!TextUtils.isEmpty(c.getImg())) {
                    GlideHelper.loadImage(c.getImg(), inflate.ivActivityMyUpdateIdcard);
                    ViewHelper.hideView(inflate.ivActivityMyUpdateIdcardAdd);
                } else {
                    inflate.ivActivityMyUpdateIdcard.setImageDrawable(null);
                    ViewHelper.showView(inflate.ivActivityMyUpdateIdcardAdd);
                }
            } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_FACE_ID)) {
                certificateInfo2 = c;
                if (!TextUtils.isEmpty(c.getImg())) {
                    GlideHelper.loadImage(c.getImg(), inflate.ivActivityMyUpdateRecognition);
                    ViewHelper.hideView(inflate.ivActivityMyUpdateRecognitionAdd);
                } else {
                    inflate.ivActivityMyUpdateRecognition.setImageDrawable(null);
                    ViewHelper.showView(inflate.ivActivityMyUpdateRecognitionAdd);
                }
            } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_HEALTH_CERT)) {
                certificateInfo3 = c;
                if (!TextUtils.isEmpty(c.getImg())) {
                    GlideHelper.loadImage(c.getImg(), inflate.ivActivityMyUpdateCertificate);
                    ViewHelper.hideView(inflate.ivActivityMyUpdateCertificateAdd);
                } else {
                    inflate.ivActivityMyUpdateCertificate.setImageDrawable(null);
                    ViewHelper.showView(inflate.ivActivityMyUpdateCertificateAdd);
                }
            } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_NO_CRIMINAL_CERT)) {
                certificateInfo4 = c;
                if (!TextUtils.isEmpty(c.getImg())) {
                    GlideHelper.loadImage(c.getImg(), inflate.ivActivityMyUpdateProof);
                    ViewHelper.hideView(inflate.ivActivityMyUpdateProofAdd);
                } else {
                    inflate.ivActivityMyUpdateProof.setImageDrawable(null);
                    ViewHelper.showView(inflate.ivActivityMyUpdateProofAdd);
                }
            } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_MATRON_CERT)) {
                certificateInfo5 = c;
                if (!TextUtils.isEmpty(c.getImg())) {
                    GlideHelper.loadImage(c.getImg(), inflate.ivActivityMyYuesaoCertificate);
                    ViewHelper.hideView(inflate.ivActivityMyYuesaoCertificateJia);
                } else {
                    inflate.ivActivityMyYuesaoCertificate.setImageDrawable(null);
                    ViewHelper.showView(inflate.ivActivityMyYuesaoCertificateJia);
                }
            } else if (TextUtils.equals(c.getAuthType(), IntCodeEnum.KEY_NURSEMAID_CERT)) {
                certificateInfo6 = c;
                if (!TextUtils.isEmpty(c.getImg())) {
                    GlideHelper.loadImage(c.getImg(), inflate.ivActivityMyCardForNurse);
                    ViewHelper.hideView(inflate.ivActivityMyCardForNurseJia);
                } else {
                    inflate.ivActivityMyCardForNurse.setImageDrawable(null);
                    ViewHelper.showView(inflate.ivActivityMyCardForNurseJia);
                }
            }
        }
    }


    @Override
    public void onRequsetCertification(int requestCode, Uri safe) {
        if (requestCode == 1) {
            GlideHelper.loadUriImage(safe, 0, inflate.ivActivityMyUpdateIdcard);
            ViewHelper.hideView(inflate.ivActivityMyUpdateIdcardAdd);
        } else if (requestCode == 2) {
            GlideHelper.loadUriImage(safe, 0, inflate.ivActivityMyUpdateRecognition);
            ViewHelper.hideView(inflate.ivActivityMyUpdateRecognitionAdd);
        } else if (requestCode == 3) {
            GlideHelper.loadUriImage(safe, 0, inflate.ivActivityMyUpdateCertificate);
            ViewHelper.hideView(inflate.ivActivityMyUpdateCertificateAdd);
        } else if (requestCode == 4) {
            GlideHelper.loadUriImage(safe, 0, inflate.ivActivityMyUpdateProof);
            ViewHelper.hideView(inflate.ivActivityMyUpdateProofAdd);
        } else if (requestCode == 5) {
            GlideHelper.loadUriImage(safe, 0, inflate.ivActivityMyYuesaoCertificate);
            ViewHelper.hideView(inflate.ivActivityMyYuesaoCertificateJia);
        } else if (requestCode == 6) {
            GlideHelper.loadUriImage(safe, 0, inflate.ivActivityMyCardForNurse);
            ViewHelper.hideView(inflate.ivActivityMyCardForNurseJia);
        }
    }

    @Override
    public void onRequestLearningExperience(LearningExperienceVm learningExperienceVm) {
        learningExperienceAdapter.setNewInstance(learningExperienceVm.getItems());
        learningExperienceAdapter.notifyDataSetChanged();

        int size = learningExperienceVm.getItems().size();
        if (size != 0) {
            inflate.tvActivityMyLearningNum.setText("总数:" + size);
        } else {
            inflate.tvActivityMyLearningNum.setText("");
        }
    }

    @Override
    public void onRequestOtherExperienceVm(OtherExperienceVm otherExperienceVm) {
        otherExperienceAdapter.setNewInstance(otherExperienceVm.getItems());
        otherExperienceAdapter.notifyDataSetChanged();


        int size = otherExperienceVm.getItems().size();
        if (size != 0) {
            inflate.tvActivityMyOtherNum.setText("总数:" + size);
        } else {
            inflate.tvActivityMyOtherNum.setText("");
        }

    }

    @Override
    public void onRequestServiceExperience(ServiceExperienceVm serviceExperienceVm) {
        ServiceExperienceVm.ServiceExperience item = serviceExperienceVm.getItem();
        if (item!=null) {
            serviceExperienceAdapter.setNewInstance(item.getOrderItems());
            serviceExperienceAdapter.notifyDataSetChanged();
            String orderCount = item.getOrderCount();
            if (TextUtils.isEmpty(orderCount)) {
                inflate.tvActivityMyServiceNum.setText("总数:" + orderCount);
            } else {
                inflate.tvActivityMyServiceNum.setText("");
            }
        }else{
            inflate.tvActivityMyServiceNum.setText("");
        }
    }

    @Override
    public void onRequestSelfExperience(SelfExperienceVm selfExperienceVm) {
        this.selfExperienceVm = selfExperienceVm;
        inflate.rvActivityMySelfExpression.setText(selfExperienceVm.getItem().getContent());

        if (!TextUtils.isEmpty(selfExperienceVm.getItem().getVideo())) {
            GlideHelper.loadVideImage(selfExperienceVm.getItem().getVideo(), inflate.ivActivityMyVideo);
        }else{
            inflate.ivActivityMyVideo.setBackground(null);
            inflate.ivActivityMyVideo.setImageDrawable(null);
        }
    }

    public void onClickRouterUserInfo(View view) {
        if (userInfoBean==null) {
            MyInfoActivity.start();
        }else{
            AddYuesaoActivity.start(userInfoBean);
        }
    }

    public void onClickOtherExperience(View view) {
        OtherExperienceActivity.start(view.getContext(), presenter.getYsId(),null);
    }

    public void onClickAddSelf(View view) {
        SelfEvaluationActivity.start(view.getContext(), presenter.getYsId(), selfExperienceVm);
    }

    public void onClickShare(View view) {
        new XPopup.Builder(view.getContext())
                .asBottomList("请选择一项", new String[]{"微信"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {
                                    presenter.createUploadUrl(inflate.view,inflate.activityMyBg, new XFunc1<String>() {
                                        @Override
                                        public void call(String s) {
                                            ShareEntity shareEntity = WXShareEntity.createImageInfo(false, s);
                                            new SocialHelper.Builder().setWxAppId("wx9366e382bb20b243")
                                                    .setWxAppSecret("756493e649571696cac2f2e67bd94687")
                                                    .build().shareWX(MyActivity.this, shareEntity, new SocialShareCallback() {
                                                @Override
                                                public void shareSuccess(int type) {
                                                    ToastHelper.showToast("分享成功");
                                                }

                                                @Override
                                                public void socialError(String msg) {

                                                }
                                            });
                                        }
                                    }, new XFunc0() {
                                        @Override
                                        public void call() {
                                            ToastHelper.showToast("截图失败,请重新尝试");
                                        }
                                    });

//                                    ShareEntity shareInfo = WXShareEntity.createImageInfo(true, R.mipmap.ic_launcher);
//                                    SocialUtil.INSTANCE.socialHelper.shareWX(MyActivity.this, shareInfo, new SocialShareCallback() {
//                                        @Override
//                                        public void shareSuccess(int type) {
//                                            ToastHelper.showToast("分享成功");
//                                        }
//
//                                        @Override
//                                        public void socialError(String msg) {
//
//                                        }
//                                    });
                                }
                            }
                        })
                .show();
    }


}
