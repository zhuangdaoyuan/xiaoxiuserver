package com.xiuxiu.confinement_nurse.ui.my;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.cretin.tools.fanpermission.FanPermissionListener;
import com.cretin.tools.fanpermission.FanPermissionUtils;
import com.xiuxiu.confinement_nurse.App;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityMyInfoLocationsBinding;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.location.LocationManager;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;
import com.xiuxiu.confinement_nurse.ui.citypicker.CityPicker;
import com.xiuxiu.confinement_nurse.ui.citypicker.adapter.OnPickListener;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.LocatedCity;
import com.xiuxiu.confinement_nurse.ui.my.adapter.LocationAdapter;
import com.xiuxiu.confinement_nurse.ui.vm.LocationVm;
import com.xiuxiu.confinement_nurse.utlis.FastClickUtil;
import com.xiuxiu.confinement_nurse.utlis.PermissionUtils;
import com.xiuxiu.confinement_nurse.utlis.Utils;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;
import static com.xiuxiu.confinement_nurse.ui.my.MyInfoActivity.LOCATION_CODE;


public class LocationActivity extends AbsBaseActivity implements OnGetPoiSearchResultListener, LocationManager.LocationListener {
    private LocationVm locationVm;
    private LocationManager locationManager;
    private ActivityMyInfoLocationsBinding inflate;
    private PoiSearch mPoiSearch;
    private City locationCity;
    private LocationAdapter locationAdapter;

    private String mCurrentMsg;
    private int index;
    public static void start(Activity context) {
        Intent starter = new Intent(context, LocationActivity.class);
        context.startActivityForResult(starter,LOCATION_CODE);
    }

    @Override
    protected View getLayoutView() {
        inflate = ActivityMyInfoLocationsBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
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
    private void loadData() {
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.onStop();
    }

    private void setListener() {
        locationAdapter.getLoadMoreModule().setEnableLoadMore(true);
        locationAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (TextUtils.isEmpty(mCurrentMsg)) {
                    locationAdapter.setNewInstance(new ArrayList<>());
                    index=0;
                    return;
                }
                index=index+1;
               startSearchData(mCurrentMsg,index);
            }
        });
        inflate.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflate.edittext.setText("");
            }
        });
        inflate.texts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocation();
            }
        });
//        inflate.edittext.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    startSearchView();
//                    return true;
//                }
//                return false;
//            }
//        });
        inflate.edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //返回true，保留软键盘。false，隐藏软键盘
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {   // 按下完成按钮，这里和上面imeOptions对应
                   startSearchView();
                }
                return true;
            }
        });
    }

    private void startSearchView() {
        if (FastClickUtil.isFastClick()) {
            return;
        }
        String s = inflate.edittext.getText().toString();
        if (TextUtils.isEmpty(s)) {
            ToastHelper.showToast("搜索内容不能为空");
            return;
        }
        if (TextUtils.equals(mCurrentMsg,s)) {
            return;
        }
        startSearchData(s,index=0);
    }

    private void startSearchData(String msg,int index){
        if (index==0) {
            showLoadingDialog();
            locationAdapter.setNewInstance(new ArrayList<>());
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        this.mCurrentMsg=msg;
        this.index=index;
        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city(locationCity.getName()) //必填
                .keyword(msg)
                .pageCapacity(10)
                .pageNum(index) // 分页编号
                .cityLimit(true)
                .scope(2));
    }

    private void initViewState() {
        locationManager = new LocationManager(((App) getContext().getApplicationContext()).locationService);
        locationManager.setLocationListener(this);


        locationAdapter=new LocationAdapter();
        inflate.recyclerView.setAdapter(locationAdapter);
        inflate.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        locationAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                PoiInfo item = locationAdapter.getItem(position);
                onRequestData(locationCity.getCode(),item.getName());
            }

            private void onRequestData(String code, String name) {
                Intent intent=new Intent();
                intent.putExtra("code",code);
                intent.putExtra("name",name);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        inflate.texts.setText(locationCity.getName());
        if (TextUtils.isEmpty(locationCity.getCityCode())) {
            requestPermission();
        }
    }

    private void initView() {
        locationVm = ViewModelProviders.of(this).get(LocationVm.class);
        this.locationCity = ModelManager.getInstance().getCacheInterface().getLocationCity();
        if (locationCity==null) {
            locationCity = new City(UserHelper.KEY_DEFAULT_CITY_NAME,
                    UserHelper.KEY_DEFAULT_CITY_NAME, "", UserHelper.KEY_DEFAULT_CITY_CODE);
        }
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        cancelLoadingDialog();
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//            mLoadIndex = 0;
            ToastHelper.showToast("未找到结果");
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            List<PoiInfo> allPoi = result.getAllPoi();
            if (index==0) {
                locationAdapter.setNewInstance(allPoi);
            }else{
                if (allPoi==null) {
                    locationAdapter.getLoadMoreModule().loadMoreComplete();
                }else{
                    locationAdapter.addData(allPoi);
                    locationAdapter.getLoadMoreModule().loadMoreEnd();
                }
            }
            return;
        }

        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            locationAdapter.getLoadMoreModule().loadMoreEnd();
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            StringBuilder mBuilder = new StringBuilder("在");
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                mBuilder.append(cityInfo.city);
                mBuilder.append(",");
            }
            mBuilder.append("找到结果");
            Toast.makeText(this, mBuilder.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        Log.i("xxxxxx",poiDetailSearchResult.toString());
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        Log.i("xxxxxx",poiIndoorResult.toString());

    }

    private void showLocation() {
        LocatedCity city;
        if (!(locationCity instanceof LocatedCity)) {
            city = new LocatedCity(locationCity.getName(), locationCity.getProvince(), locationCity.getCode());
        }else{
            city= (LocatedCity) locationCity;
        }
        CityPicker.from(this)
                .setLocatedCity(city)
                .setHotCities(null)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        locationCity=data;
                        inflate.texts.setText(data.getName());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onLocate() {

                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";

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

    protected void loadLocationManager() {
        if (isOpenGps()) {
            locationManager.onStart();
        } else {
            ToastHelper.showToast("为了更好的服务，请开启GPS定位");
        }
    }

    private void requestPermission() {

        if (PermissionUtils.isGranted(needPermissions)) {
            loadLocationManager();
            return;
        }
        FanPermissionUtils fanPermissionUtils = FanPermissionUtils.with(this);
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
                    city1.setCityCode(String.valueOf(131));
                    locationCity=city1;
                    inflate.texts.setText(locationCity.getName());
                }
            }
        }).startCheckPermission();
    }


    @Override
    public void location(double latitude, double longitude, String locationCitys, String cityCode, String province) {
        ((App) Utils.getApp()).locationService.stop();
        locationVm.findCity(locationCitys, new XFunc1<City>() {
            @Override
            public void call(City city) {
                city.setLatitude(latitude);
                city.setLongitude(longitude);
                city.setCityCode(cityCode);
                locationCity=city;
                inflate.texts.setText(locationCity.getName());
            }
        });
    }
}
