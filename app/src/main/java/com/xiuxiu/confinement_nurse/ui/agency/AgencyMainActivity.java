package com.xiuxiu.confinement_nurse.ui.agency;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.monster.gamma.core.Gamma;
import com.monster.gamma.core.LoadService;
import com.xiuxiu.confinement_nurse.Constants;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityMainAgencyBinding;
import com.xiuxiu.confinement_nurse.help.FragmentHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.ui.MainActivity;
import com.xiuxiu.confinement_nurse.ui.area.AreaContract;
import com.xiuxiu.confinement_nurse.ui.area.AreaPresenter;
import com.xiuxiu.confinement_nurse.ui.area.vm.CityVm;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.my.CenterFragment;
import com.xiuxiu.confinement_nurse.ui.news.NewsFragment;
import com.xiuxiu.confinement_nurse.ui.office.OfficeFragment;
import com.xiuxiu.confinement_nurse.ui.view.BottomBar;
import com.xiuxiu.confinement_nurse.ui.vm.LocationVm;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_LOGIN_SUCCESS;

/**
 * 机构首页
 */
public class AgencyMainActivity extends AbsBaseActivity implements AreaContract.IView {


    public static void start(Context context) {
        Intent starter = new Intent(context, AgencyMainActivity.class);
        context.startActivity(starter);
    }

    private AreaContract.IPresenter presenter;
    private  ActivityMainAgencyBinding  mViewBinding;
    private Fragment mCurrentFragment;
    private LocationVm locationVm;

    @Override
    protected View getLayoutView() {
        mViewBinding = ActivityMainAgencyBinding.inflate(LayoutInflater.from(this));
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
        mViewBinding.bottomBar.setFistText("人员列表");

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
        return R.color.color_FFFFFF_a100;
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
                        fragment = AgencyNewsFragment.newInstance();
                        break;
                    case R.id.tab_center:
                        fragment = MechanismCenterFragment.newInstance();
                        break;
                    case R.id.tab_order:
                    default:
                        fragment = MechanismMainFragment.newInstance();
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
        if (requestCode == 1 && resultCode == KEY_LOGIN_SUCCESS) {
            if (UserHelper.isLogin()) {
                UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                if (TextUtils.equals(userBean.getUserType(), Constants.KEY_AGENCY)) {
                    AgencyMainActivity.start(this);
                    finish();
                }else{
                    MainActivity.start(this);
                    finish();
                }
            }
        }
    }

}
