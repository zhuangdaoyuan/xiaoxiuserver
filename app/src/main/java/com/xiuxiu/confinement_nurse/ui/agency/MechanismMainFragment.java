package com.xiuxiu.confinement_nurse.ui.agency;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.cretin.tools.fanpermission.FanPermissionListener;
import com.cretin.tools.fanpermission.FanPermissionUtils;
import com.monster.gamma.callback.LayoutNoLogin;
import com.xiuxiu.confinement_nurse.App;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.FragmentMechainsmMainBinding;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.location.LocationManager;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.model.event.YuesaoInfoEvent;
import com.xiuxiu.confinement_nurse.ui.agency.adapter.MechanismItemAdapter;
import com.xiuxiu.confinement_nurse.ui.agency.user.AddYuesaoActivity;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseFragment;
import com.xiuxiu.confinement_nurse.ui.base.SpacesItemDecoration;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;
import com.xiuxiu.confinement_nurse.ui.login.LoginActivity;
import com.xiuxiu.confinement_nurse.ui.my.MyActivity;
import com.xiuxiu.confinement_nurse.ui.order.OrderListActivity;
import com.xiuxiu.confinement_nurse.ui.schedule.MyScheduleActivity;
import com.xiuxiu.confinement_nurse.ui.service.MyServiceActivity;
import com.xiuxiu.confinement_nurse.ui.vm.LocationVm;
import com.xiuxiu.confinement_nurse.utlis.PermissionUtils;
import com.xiuxiu.confinement_nurse.utlis.Utils;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.List;

public class MechanismMainFragment extends AbsBaseFragment implements MechanismMainContract.IView, LocationManager.LocationListener {

    private FragmentMechainsmMainBinding inflate;
    private MechanismMainContract.IPresenter mPresenter;
    private MechanismItemAdapter mAdapter;
    private LocationManager locationManager;
    private LocationVm locationVm;

    public static MechanismMainFragment newInstance() {
        MechanismMainFragment fragment = new MechanismMainFragment();
        return fragment;
    }

    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        inflate = FragmentMechainsmMainBinding.inflate(inflater, container, false);
        return inflate.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.onStop();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData(savedInstanceState);
        initViewState();
        setListener();
        loadData();

        locationVm = ViewModelProviders.of(getActivity()).get(LocationVm.class);

        City locationCity = ModelManager.getInstance().getCacheInterface().getLocationCity();
        locationVm.setCityLiveData(locationCity);
        locationVm.setLocationCity(locationCity);


        locationManager = new LocationManager(((App) getContext().getApplicationContext()).locationService);
        locationManager.setLocationListener(this);


        requestPermission();
    }

    private void loadData() {
        if (UserHelper.isLogin()) {
            mPresenter.requestList();
        } else {
            loadService.showCallback(LayoutNoLogin.class);
        }
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        Class currentCallback = loadService.getCurrentCallback();
        if (currentCallback == LayoutNoLogin.class) {
            LoginActivity.start(getActivity());
        } else {
            loadData();
        }
    }

    private void setListener() {
        inflate.fragmentMechainsmMainAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //增加月嫂
                AddYuesaoActivity.start();
            }
        });
        inflate.srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestList();
            }
        });
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.onLoadMore();
            }
        });
        EventBus.LoginEvent().observe(this, loginEvent -> {
            if (UserHelper.isJiGou()) {
                loadData();
            }
        });
        EventBus.LogoutEvent().observe(this, loginEvent -> loadData());

        EventBus.UpdateEvent().observe(this, loginEvent -> loadData());

        mAdapter.addChildClickViewIds( R.id.item_yuesao2, R.id.item_yuesao3, R.id.item_yuesao4);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                int id = view.getId();
                Object item = adapter.getItem(position);
                if (item instanceof UserInfoBean) {
//                    if (id == R.id.item_yuesao1) {
//                        LearningExperenceListActivity.start(getContext(),((UserInfoBean) item).getMatronId());
//                    } else

                        if (id == R.id.item_yuesao2) {
                        MyServiceActivity.start(getContext(),((UserInfoBean) item).getMatronId());
                    } else if (id == R.id.item_yuesao3) {
                        OrderListActivity.start(getContext(),((UserInfoBean) item).getMatronId(),((UserInfoBean) item).getUserName());
                    } else if (id == R.id.item_yuesao4) {
                        MyScheduleActivity.start(getContext(),((UserInfoBean) item).getMatronId(),((UserInfoBean) item).getUserName());
                    }
                }

            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Object item = adapter.getItem(position);
                if (item instanceof UserInfoBean) {
//                    AddYuesaoActivity.start((UserInfoBean) item);
//                    LearningExperenceListActivity.start(getContext(),((UserInfoBean) item).getMatronId());
                    MyActivity.start(((UserInfoBean) item));
                }
            }
        });
    }

    private void initViewState() {
        inflate.rvNewDetail.setAdapter(mAdapter = new MechanismItemAdapter());
        inflate.rvNewDetail.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        inflate.rvNewDetail.addItemDecoration(new SpacesItemDecoration(ResHelper.pt2Px(46), ResHelper.pt2Px(46),
                ResHelper.pt2Px(24), ResHelper.pt2Px(102)));
        mAdapter.addChildClickViewIds(R.id.tv_item_order_recommend_delivery);
    }

    private void initData(Bundle savedInstanceState) {
    }

    private void initView(View view) {
        mPresenter = new MechanismMainPresenter(this);
    }

    @Override
    public void onRequestDataList(List<UserInfoBean> s) {
        inflate.srl.setRefreshing(false);
        mAdapter.setNewInstance(s);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestComplete() {
        mAdapter.getLoadMoreModule().loadMoreComplete();
    }

    @Override
    public void onRequestPageData(List<UserInfoBean> officeBeans) {
        mAdapter.addData(officeBeans);
        mAdapter.getLoadMoreModule().loadMoreEnd();
    }

    @Override
    public void onRequestPageDataError() {
        inflate.srl.setRefreshing(false);
        mAdapter.getLoadMoreModule().setEnableLoadMore(true);
        mAdapter.getLoadMoreModule().loadMoreFail();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    @Override
    public void location(double latitude, double longitude, String locationCity, String cityCode, String province) {
        ((App) Utils.getApp()).locationService.stop();
        Log.i("xqy", "locationCity:" + locationCity);
        locationVm.findCity(locationCity, new XFunc1<City>() {
            @Override
            public void call(City city) {
                Log.i("xqy", "locationCity:" + locationCity);
                city.setLatitude(latitude);
                city.setLongitude(longitude);
                city.setCityCode(cityCode);

                locationVm.setCityLiveData(city);
                locationVm.setLocationCity(city);
            }
        });

    }

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private boolean isOpenGps() {
        android.location.LocationManager locationManager
                = (android.location.LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    private void requestPermission() {
        if (PermissionUtils.isGranted(needPermissions)) {
            loadLocationManager();
            return;
        }
        FanPermissionUtils fanPermissionUtils = FanPermissionUtils.with(getActivity());
        for (int i = 0; i < needPermissions.length; i++) {
            fanPermissionUtils.addPermissions(needPermissions[i]);
        }
        if (Build.VERSION.SDK_INT > 28 && getContext().getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            fanPermissionUtils.addPermissions(BACKGROUND_LOCATION_PERMISSION);
        }
        fanPermissionUtils.setPermissionsCheckListener(new FanPermissionListener() {
            @Override
            public void permissionRequestSuccess() {
                loadLocationManager();
            }

            @Override
            public void permissionRequestFail(String[] grantedPermissions, String[] deniedPermissions, String[] forceDeniedPermissions) {
                if (grantedPermissions.length == needPermissions.length) {
                    loadLocationManager();
                } else {
                    City city1 = new City(UserHelper.KEY_DEFAULT_CITY_NAME,
                            UserHelper.KEY_DEFAULT_CITY_NAME, "", UserHelper.KEY_DEFAULT_CITY_CODE);
                    locationVm.setCityLiveData(city1);
                    locationVm.setLocationCity(city1);
                }
            }
        }).startCheckPermission();

    }

    protected void loadLocationManager() {
        if (isOpenGps()) {
            locationManager.onStart();
        } else {
            ToastHelper.showToast("为了更好的服务，请开启GPS定位");
        }
    }
}
