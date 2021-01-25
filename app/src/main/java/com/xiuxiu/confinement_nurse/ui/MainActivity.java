package com.xiuxiu.confinement_nurse.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.xiuxiu.confinement_nurse.Constants;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityMainBinding;
import com.xiuxiu.confinement_nurse.help.FragmentHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.ui.agency.AgencyMainActivity;
import com.xiuxiu.confinement_nurse.ui.area.AreaContract;
import com.xiuxiu.confinement_nurse.ui.area.AreaPresenter;
import com.xiuxiu.confinement_nurse.ui.area.vm.CityVm;
import com.xiuxiu.confinement_nurse.ui.vm.LocationVm;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.my.CenterFragment;
import com.xiuxiu.confinement_nurse.ui.news.NewsFragment;
import com.xiuxiu.confinement_nurse.ui.office.OfficeFragment;
import com.xiuxiu.confinement_nurse.ui.view.BottomBar;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_LOGIN_SUCCESS;


public class MainActivity extends AbsBaseActivity implements AreaContract.IView {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
    ActivityMainBinding mViewBinding;
    private Fragment mCurrentFragment;
    private LocationVm locationVm;
    private AreaContract.IPresenter presenter;

    @Override
    protected View getLayoutView() {
        mViewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        return mViewBinding.getRoot();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置软键盘的模式为适应屏幕模式
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView();
        setListener();
        mViewBinding.bottomBar.loadData();


        loadData();
        onRequestPageSuccess();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadData() {
        locationVm = ViewModelProviders.of(this).get(LocationVm.class);

        presenter.requestLocationData();
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.color_F7D1FF_a100;
    }

    private void initView() {
        presenter = new AreaPresenter(this);
    }

    private void setListener() {
        mViewBinding.bottomBar.setOnSelectItem(new BottomBar.OnSelectItem() {
            @Override
            public void onSelect(int position) {
                int id;
                if (position == 0) {
                    id = R.id.tab_order;
                } else if (position == 1) {
                    id = R.id.tab_news;
                } else {
                    id = R.id.tab_center;
                }
                switchFragment(id);
            }
        });
    }

    private void switchFragment(int tabId) {
        FragmentHelper.showCurrentFragment(getSupportFragmentManager(), String.valueOf(tabId), new FragmentHelper.FragmentCallBack() {
            @Override
            public Fragment createFragment(String tag) {
                Fragment fragment;
                switch (Integer.parseInt(tag)) {
                    case R.id.tab_news:
                        fragment = NewsFragment.newInstance();
                        break;
                    case R.id.tab_center:
                        fragment = CenterFragment.newInstance();
                        break;
                    case R.id.tab_order:
                    default:
                        fragment = OfficeFragment.newInstance();
                        break;
                }
                mCurrentFragment = fragment;
                return fragment;
            }

            @Override
            public Integer context() {
                return R.id.fl_content;
            }

            @Override
            public void selectFragment(Fragment baseFragment) {
                mCurrentFragment = baseFragment;
            }
        });
    }

    @Override
    public void onRequestCitys(CityVm locationVm) {

    }

    @Override
    public void onRequestLocations(com.xiuxiu.confinement_nurse.ui.area.vm.LocationVm locationVm) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==KEY_LOGIN_SUCCESS) {
            if (UserHelper.isLogin()) {
                UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                if (TextUtils.equals(userBean.getUserType(), Constants.KEY_AGENCY)) {
                    AgencyMainActivity.start(this);
                    finish();
                }else{
                    MainActivity.start(this);
                }
            }
        }
    }
}