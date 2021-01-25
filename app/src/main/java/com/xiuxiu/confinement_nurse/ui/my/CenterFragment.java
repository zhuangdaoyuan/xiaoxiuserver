package com.xiuxiu.confinement_nurse.ui.my;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.monster.gamma.callback.LayoutEmpty;
import com.monster.gamma.callback.LayoutError;
import com.monster.gamma.callback.LayoutNoLogin;
import com.monster.share.SocialHelper;
import com.monster.share.callback.SocialShareCallback;
import com.monster.share.entities.ShareEntity;
import com.monster.share.entities.WXShareEntity;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.databinding.FragmentCenterBinding;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.model.IntCodeEnum;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.model.event.LoginEvent;
import com.xiuxiu.confinement_nurse.model.event.UserInfoEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseFragment;
import com.xiuxiu.confinement_nurse.ui.login.LoginActivity;
import com.xiuxiu.confinement_nurse.ui.my.vm.TeamVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.UserStateInfoCodeVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.UserStateInfoVm;
import com.xiuxiu.confinement_nurse.ui.order.OrderListActivity;
import com.xiuxiu.confinement_nurse.ui.schedule.MyScheduleActivity;
import com.xiuxiu.confinement_nurse.ui.service.MyServiceActivity;
import com.xiuxiu.confinement_nurse.ui.set.SetActivity;
import com.xiuxiu.confinement_nurse.ui.user.UserInfoContract;
import com.xiuxiu.confinement_nurse.ui.user.UserInfoPresenter;
import com.xiuxiu.confinement_nurse.utlis.BigDecimalUtils;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;


public class CenterFragment extends AbsBaseFragment implements CenterContract.IView {
    public static CenterFragment newInstance() {
        Bundle args = new Bundle();
        CenterFragment fragment = new CenterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private FragmentCenterBinding mViewBinding;
    private CenterContract.IPresenter centerPresenter;
    private UserInfoContract.IPresenter userPresenter;

    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        mViewBinding = FragmentCenterBinding.inflate(inflater, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData(savedInstanceState);
        initViewState();
        setListener();
        loadData();
    }

    private void loadData() {
        if (UserHelper.isLogin()) {
            onRequestPageSuccess();
            loadUserInfo(ModelManager.getInstance().getUserInterface().requestCurrentUserBean());
            centerPresenter.requestData();
            centerPresenter.requestInvitationCode();
        } else {
            loadService.showCallback(LayoutNoLogin.class);
        }
        userPresenter.requestUserInfoByCertificate(aBoolean ->
                loadUserCertificateInfo(ModelManager.getInstance().getUserInterface().requestCurrentUserBean()), null);
    }

    private void setListener() {
        mViewBinding.tvFragmentCenterUserName.setOnClickListener(v -> MyInfoActivity.start());
        mViewBinding.vFragment.setOnClickListener(v -> MyScheduleActivity.start(v.getContext()));
        mViewBinding.vFragment1.setOnClickListener(v -> MyScheduleActivity.start(v.getContext()));
        mViewBinding.vFragment2.setOnClickListener(v -> MyScheduleActivity.start(v.getContext()));
        mViewBinding.vFragment3.setOnClickListener(v -> MyScheduleActivity.start(v.getContext()));

        mViewBinding.fragmentCenterSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetActivity.start(v.getContext());
            }
        });

        EventBus.UpdateUserInfoByName().observe(this, userInfoEvent -> {
            UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
            loadUserName(userBean);
        });
        EventBus.UpdateUserInfoByAvatarList().observe(this, userInfoEvent -> {
            UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
            GlideHelper.loadImage(userBean.getItem().getUserAvatar(), mViewBinding.cvActivityMyFace);
        });
        EventBus.LoginEvent().observe(this, loginEvent -> {
           if( UserHelper.isYuesao()){
                loadData();
            }

        });
        EventBus.LogoutEvent().observe(this, loginEvent -> {
            loadData();
        });


        mViewBinding.flMyInfo.setOnClickListener(v -> {
            userPresenter.requestUserInfoByCertificate(aBoolean -> {
                if (aBoolean) {
                    MyServiceActivity.start(v.getContext());
                } else {
                    DialogHelper.authenticationRequired(getContext(), () -> MyActivity.start());
                }
            }, () -> ToastHelper.showToast("检查失败，请重试"));
        });
        mViewBinding.vFragmentCenterInfo.setOnClickListener(v -> {
            MyActivity.start();
        });
        mViewBinding.tvFragmentCenterUserTag1.setOnClickListener(v -> MyActivity.start());
        mViewBinding.tvFragmentCenterUserTag2.setOnClickListener(v -> MyActivity.start());

        mViewBinding.vFragmentCenterBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mViewBinding.cvActivityMyFace.setOnClickListener(v -> MyInfoActivity.start());
        mViewBinding.vFragmentCenterBg.setOnClickListener(v -> OrderListActivity.start(v.getContext()));
        mViewBinding.tvFragmentCenterOrderNum.setOnClickListener(v -> OrderListActivity.start(v.getContext()));


        mViewBinding.tvFragmentCenterHasInvitationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputConfirmPopupView inputConfirmPopupView = new XPopup.Builder(v.getContext()).asInputConfirm("加入团队", "请输入团队邀请码。",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                centerPresenter.requestJoinTheTeam(text);
                            }
                        });
                inputConfirmPopupView
                        .show();
                inputConfirmPopupView.post(new Runnable() {
                    @Override
                    public void run() {
                        EditText editText = inputConfirmPopupView.getEditText();
                        if (editText != null) {
                            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                        }
                    }
                });
            }
        });
        mViewBinding.tvFragmentCenterHasTeamTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamVm == null) {
                    ToastHelper.showToast("请重试");
                    centerPresenter.requestInvitationCode();
                    return;
                }
                TeamVm.Team team = teamVm.getTeam();
                String msg = "团队邀请码:" + (team == null ? "" : team.getCode());
                new XPopup.Builder(v.getContext())
                        .asBottomList(team == null ? "" : team.getGroupName(), new String[]{msg},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {

                                    }
                                })
                        .show();
            }
        });
    }

    private void initViewState() {
        centerPresenter = new CenterPresenter(this);
        userPresenter = new UserInfoPresenter(this);
    }

    private void initView(View view) {
    }

    private void initData(Bundle savedInstanceState) {
    }


    private void loadUserInfo(UserBean userBean) {
        loadUserName(userBean);


        String avatarList = userBean.getItem().getAvatarList();
        if (!TextUtils.isEmpty(avatarList)) {
            String[] split = avatarList.split(",");
            GlideHelper.loadImage(split[0], mViewBinding.cvActivityMyFace);
        }
    }

    private void loadUserName(UserBean userBean) {
        mViewBinding.tvFragmentCenterUserName.setText(TextUtils.isEmpty(userBean.getItem().getUserName()) ? "未填写" : userBean.getItem().getUserName());
    }

    private void loadUserCertificateInfo(UserBean userBean) {
        if (UserHelper.whetherItIsCertified(userBean.getCertificateInfoBean(), IntCodeEnum.KEY_MATRON_CERT)) {
            ViewHelper.showView(mViewBinding.tvFragmentCenterUserTag1);
        } else {
            ViewHelper.hideView(mViewBinding.tvFragmentCenterUserTag1);
        }

        if (UserHelper.whetherItIsCertified(userBean.getCertificateInfoBean(), IntCodeEnum.KEY_NURSEMAID_CERT)) {
            ViewHelper.showView(mViewBinding.tvFragmentCenterUserTag2);
        } else {
            ViewHelper.hideView(mViewBinding.tvFragmentCenterUserTag2);
        }
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        Class currentCallback = loadService.getCurrentCallback();
        if (currentCallback == LayoutNoLogin.class) {
            LoginActivity.start(getActivity());
        } else if (currentCallback == LayoutEmpty.class) {
            loadData();
        } else if (currentCallback == LayoutError.class) {
            loadData();
        }
    }

    @Override
    public void onResquestUserInfo(UserStateInfoVm.UserStateInfo item) {
        try {
            mViewBinding.tvFragmentCenterTotalAssets.setText("¥"+BigDecimalUtils.div(item.getTotalBalance(),String.valueOf(100),2));
            mViewBinding.tvFragmentCenterHasArrived.setText("¥"+BigDecimalUtils.div(item.getAvailableBalance(),String.valueOf(100),2));
        }catch (Exception e){
            mViewBinding.tvFragmentCenterTotalAssets.setText("");
            mViewBinding.tvFragmentCenterHasArrived.setText("");
        }

        mViewBinding.tvFragmentCenterOrderNum.setText(item.getOrderNum());
//        mViewBinding.tvFragmentCenterHasTeam.setText(item.getGroupMatronNum());
    }

    @Override
    public void onResquestUserInfoCode(UserStateInfoCodeVm s) {
//        mViewBinding.tvFragmentCenterHasInvitationCode.setText(s.getItem());
    }

    private TeamVm teamVm;

    @Override
    public void onRequestUserTeam(TeamVm s) {
        teamVm = s;
    }
}
