package com.xiuxiu.confinement_nurse.ui.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.RongYunHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.cache.CacheInterface;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;
import com.xiuxiu.confinement_nurse.ui.news.vm.TokenVm;
import com.xiuxiu.confinement_nurse.ui.news.vm.UserInfoVm;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * 人工
 */
public class ArtificialActivity extends BaseActivity implements NewsContract.IView {
    public static void start(Context context) {
        RongIM.getInstance().startPrivateChat(context, "-999", "在线客服");
    }

    private NewsPresenter newsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artificial);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    public void onBack(View view) {
        finish();
    }

    private void loadData() {
        newsPresenter = new NewsPresenter(this);
        String rongyunToken = ModelManager.getInstance().getCacheInterface().getRongyunToken();
        RongYunHelper.customerService(rongyunToken, new XFunc1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer == 1) {
                    newsPresenter.requestCustomerServiceToken();
                }
            }
        });
    }

    private void setListener() {
    }

    private void initViewState() {
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            String lastPathSegment = data.getLastPathSegment();
            Conversation.ConversationType mConversationType = Conversation.ConversationType.valueOf(lastPathSegment.toUpperCase(Locale.getDefault()));

            String mTargetId = intent.getData().getQueryParameter("targetId");

            ConversationFragment conversationFragment = new ConversationFragment();


            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                    .appendQueryParameter("targetId", mTargetId).build();

            conversationFragment.setUri(uri);


            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, conversationFragment, "conversation");
            transaction.commit();
        }

    }

    private void initView() {
    }

    @Override
    public void onBackPressed() {
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentByTag("conversation");
        if (fragment != null && !fragment.onBackPressed()) {
            finish();
        }
    }


    @Override
    public void onRequestUserToken(TokenVm s) {

    }

    @Override
    public void onRequestCustomerServiceToken(TokenVm s) {
        RongYunHelper.customerService(s.getItem(), new XFunc1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer == 1) {
                    newsPresenter.requestCustomerServiceToken();
                }
            }
        });
    }

    @Override
    public void onRequestUserInfo(UserInfoVm s) {

    }
}
