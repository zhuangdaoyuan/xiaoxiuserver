package com.xiuxiu.confinement_nurse.ui.agency;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.monster.gamma.callback.LayoutNoLogin;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.FragmentMechanismCenterBinding;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.AgencyInfoBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseFragment;
import com.xiuxiu.confinement_nurse.ui.login.LoginActivity;
import com.xiuxiu.confinement_nurse.ui.order.OrderListActivity;
import com.xiuxiu.confinement_nurse.ui.set.SetActivity;

/**
 * 机构个人中心
 */
public class  MechanismCenterFragment   extends AbsBaseFragment {

    private  FragmentMechanismCenterBinding inflate;

    public static MechanismCenterFragment newInstance() {
        MechanismCenterFragment fragment = new MechanismCenterFragment();
        return fragment;
    }
    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        inflate = FragmentMechanismCenterBinding.inflate(inflater, container, false);
        return inflate.getRoot();
    }

    @Override
    protected Object getTargetView() {
        return  inflate.fragmentMechanismContent;
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
            UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
            inflate.fragmentMechanismName.setText(userBean.getName());
            AgencyInfoBean agencyInfoBean = userBean.getAgencyInfoBean();
            if (agencyInfoBean!=null) {
                String orgImg = agencyInfoBean.getOrgImg();
                if (TextUtils.isEmpty(orgImg)) {
                    orgImg=agencyInfoBean.getForeImg();
                }
                if (TextUtils.isEmpty(orgImg)) {
                    orgImg=agencyInfoBean.getTailImg();
                }
                Glide.with(inflate.fragmentMechanismAvatar).load(orgImg)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .placeholder(R.color.color_backdrop_placeholder)
                        .error(R.color.color_backdrop_placeholder)
                        .into(inflate.fragmentMechanismAvatar);
            }
            onRequestPageSuccess();
        }else{
            loadService.showCallback(LayoutNoLogin.class);
        }
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        Class currentCallback = loadService.getCurrentCallback();
        if (currentCallback== LayoutNoLogin.class) {
            LoginActivity.start(getActivity());
        }
    }

    private void setListener() {
        EventBus.LoginEvent().observe(this, loginEvent -> loadData());
        EventBus.LogoutEvent().observe(this, loginEvent -> loadData());
        inflate.fragmentMechanismAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //机构信息
                AgencyInfoActivity.start(v.getContext());
            }
        });
        inflate.vFragmentCenterBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //订单
                UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                OrderListActivity.start(getContext(),userBean.getAgencyInfoBean().getId(),userBean.getAgencyInfoBean().getTitle());
            }
        });
        inflate.vFragmentCenterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //月搜列表
                SetActivity.start(v.getContext());
            }
        });
        inflate.fragmentCenterSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetActivity.start(v.getContext());
            }
        });
    }

    private void initViewState() {

    }

    private void initData(Bundle savedInstanceState) {
    }

    private void initView(View view) {
    }
}
