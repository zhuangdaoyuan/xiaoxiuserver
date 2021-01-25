package com.xiuxiu.confinement_nurse.ui;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xiuxiu.confinement_nurse.Constants;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.ui.MainActivity;
import com.xiuxiu.confinement_nurse.ui.agency.AgencyMainActivity;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class WelcomeActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void loadData() {
        Observable.timer(2,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (!ModelManager.getInstance().getCacheInterface().getIsGuide()) {
                            GuideActivity.start(WelcomeActivity.this);
                        } else {
                            if (UserHelper.isLogin()) {
                                UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                                if (TextUtils.equals(userBean.getUserType(), Constants.KEY_AGENCY)) {
                                    AgencyMainActivity.start(WelcomeActivity.this);
                                    finish();
                                    return;
                                }
                            }
                            MainActivity.start(WelcomeActivity.this);
                        }
                        finish();
                    }
                });

    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.color_FFFFFF_a100;
    }
}
