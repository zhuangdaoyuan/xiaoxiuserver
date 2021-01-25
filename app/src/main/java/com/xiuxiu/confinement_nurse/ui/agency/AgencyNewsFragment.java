package com.xiuxiu.confinement_nurse.ui.agency;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.monster.gamma.callback.LayoutNoLogin;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.databinding.FragmentNews2Binding;
import com.xiuxiu.confinement_nurse.databinding.FragmentNewsBinding;
import com.xiuxiu.confinement_nurse.help.RongYunHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseFragment;
import com.xiuxiu.confinement_nurse.ui.news.ArtificialActivity;
import com.xiuxiu.confinement_nurse.ui.news.vm.TokenVm;
import com.xiuxiu.confinement_nurse.ui.news.vm.UserInfoVm;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 消息首页
 */
public class AgencyNewsFragment extends AbsBaseFragment implements AgencyNewsContract.IView {
    private AgencyNewsPresenter newsPresenter;

    public static AgencyNewsFragment newInstance() {
        Bundle args = new Bundle();
        AgencyNewsFragment fragment = new AgencyNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentNews2Binding mViewBinding;


    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        mViewBinding = FragmentNews2Binding.inflate(inflater, container, false);
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


    private void setListener() {
        EventBus.LoginEvent()
                .observe(this, loginEvent ->{
                    if( UserHelper.isYuesao()){
                        loadData();
                    }
                });
        EventBus.LogoutEvent()
                .observe(this, loginEvent -> loadData());
        mViewBinding.flFragmentNewsBack.setOnClickListener(v -> ArtificialActivity.start(v.getContext()));
        mViewBinding.ivFragmentNewsBack.setOnClickListener(v -> ArtificialActivity.start(v.getContext()));
    }

    private void initViewState() {
        newsPresenter = new AgencyNewsPresenter(this);
        boolean isCacheUserInfo = true;
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            /**
             * 获取设置用户信息. 通过返回的 userId 来封装生产用户信息.
             * @param userId 用户 ID
             */
            @Override
            public UserInfo getUserInfo(String userId) {
                newsPresenter.requestUserInfo(userId);
                if (TextUtils.equals(userId,"-999")) {
                    UserInfo userInfo = new UserInfo(userId, "在线客服", Uri.parse(""));
                    return userInfo;
                }
                return null;
            }

        }, isCacheUserInfo);


        RongYunHelper.enterFragment(this);
    }

    private void initView(View view) {
    }

    private void initData(Bundle savedInstanceState) {
    }

    private void loadData() {
        if (!UserHelper.isLogin()) {
            loadService.showCallback(LayoutNoLogin.class);
        } else {
            onRequestPageSuccess();
            newsPresenter.requestUserToken(ModelManager.getInstance().getUserInterface().requestCurrentUserBean().getId());
        }
        newsPresenter.requestCustomerServiceToken();
    }


    @Override
    public void onRequestUserToken(TokenVm s) {
        RongYunHelper.customerService(s.getItem(), new XFunc1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer == 1) {
                    loadData();
                }
            }
        });
    }

    @Override
    public void onRequestCustomerServiceToken(TokenVm s) {

    }

    @Override
    public void onRequestUserInfo(UserInfoVm s) {
        UserInfoVm.UserInfo safe = CollectionUtil.getSafe(s.getItems(), 0, null);
        if (safe!=null) {
            UserInfo userInfo = new UserInfo(safe.getUserId(), safe.getNickName(), Uri.parse(safe.getPortrait()));
            RongIM.getInstance().refreshUserInfoCache(userInfo);
        }
    }
}
