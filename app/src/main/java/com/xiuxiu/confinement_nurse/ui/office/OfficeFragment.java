package com.xiuxiu.confinement_nurse.ui.office;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.cretin.tools.fanpermission.FanPermissionListener;
import com.cretin.tools.fanpermission.FanPermissionUtils;
import com.xiuxiu.confinement_nurse.App;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.FragmentOrderBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.ui.citypicker.CityPicker;
import com.xiuxiu.confinement_nurse.ui.citypicker.adapter.OnPickListener;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.LocateState;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.LocatedCity;
import com.xiuxiu.confinement_nurse.ui.order.OrderListActivity;
import com.xiuxiu.confinement_nurse.ui.vm.LocationVm;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseFragment;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderTitleVm;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.PermissionUtils;
import com.xiuxiu.confinement_nurse.utlis.Utils;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;
import com.xiuxiu.confinement_nurse.help.location.LocationManager;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_PX;


public class OfficeFragment extends AbsBaseFragment implements OfficeContract.IView, LocationManager.LocationListener {

    private LocationVm locationVm;

    private LocationManager locationManager;


    public static OfficeFragment newInstance() {

        Bundle args = new Bundle();

        OfficeFragment fragment = new OfficeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentOrderBinding mViewBinding;
    private OrderViewPagerAadpter orderViewPagerAadpter;
    private OrderCommonNavigatorAdapter orderCommonNavigatorAdapter;
    private OfficeContract.IPresenter presenter;
    private CommonNavigator commonNavigator;
    private int mTempPosition = -1;


    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        mViewBinding = FragmentOrderBinding.inflate(inflater, container, false);
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

        locationManager = new LocationManager(((App) getContext().getApplicationContext()).locationService);
        locationManager.setLocationListener(this);

        requestPermission();

    }

    private void loadData() {
        City locationCity = ModelManager.getInstance().getCacheInterface().getLocationCity();
        locationVm.setCityLiveData(locationCity);
        locationVm.setLocationCity(locationCity);

        presenter.requestTitleData();
    }

    private void setListener() {
        locationVm.getCityLiveData().observe(this, new Observer<City>() {
            @Override
            public void onChanged(City city) {
                if (city == null) {
                    return;
                }
                if (!(city instanceof LocatedCity)) {
                    city = new LocatedCity(city.getName(), city.getProvince(), city.getCode());
                }
                CityPicker.from(getActivity()).locateComplete((LocatedCity) city, LocateState.SUCCESS);
                mViewBinding.tvMainLocation.setText(city.getName());
            }
        });
        mViewBinding.fragmentOrderMyLisg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListActivity.start(v.getContext());
            }
        });
        mViewBinding.fragmentOrderMyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });
        mViewBinding.llFragmentOrderLocation.setOnClickListener(v -> showLocation());
        mViewBinding.mainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mTempPosition != -1) {
                    setTitlteAnim(1);
                }
                mTempPosition = position;
                setTitlteAnim(1.2f);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    private void showFilter() {
        FilterActivity.start(getActivity());
    }


    private void setTitlteAnim(float i) {
        View childAt = commonNavigator.getTitleContainer().getChildAt(mTempPosition);
        if (childAt != null) {
            int width = childAt.getWidth();
            int height = childAt.getHeight();
            childAt.setPivotX(width / 2);
            childAt.setPivotY(height);
            ViewCompat.animate(childAt).scaleY(i).scaleX(i).setDuration(200).start();

            if (childAt instanceof TextView) {
                TextView textView = (TextView) childAt;
                textView.setTypeface(Typeface.defaultFromStyle(i != 1 ? Typeface.BOLD : Typeface.NORMAL));
            }
        }
    }

    private void initViewState() {
        locationVm = ViewModelProviders.of(getActivity()).get(LocationVm.class);

        presenter = new OfficePresenter(this);
        orderViewPagerAadpter = new OrderViewPagerAadpter(getChildFragmentManager());
        mViewBinding.mainViewpager.setAdapter(orderViewPagerAadpter);
        mViewBinding.mainViewpager.setOffscreenPageLimit(3);
        commonNavigator = new CommonNavigator(getContext());
        orderCommonNavigatorAdapter = new OrderCommonNavigatorAdapter(new XFunc2<Integer, OrderTitleVm>() {
            @Override
            public void call(Integer integer, OrderTitleVm orderTitleVm) {
                mViewBinding.mainViewpager.setCurrentItem(integer);
            }
        });
        commonNavigator.setAdapter(orderCommonNavigatorAdapter);
        mViewBinding.magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mViewBinding.magicIndicator, mViewBinding.mainViewpager);
    }


    @Override
    public void onStop() {
        super.onStop();
        locationManager.onStop();
    }

    private void initView(View view) {
    }

    private void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onRequestTitleData(List<OrderTitleVm> orderTitleVm) {
        orderViewPagerAadpter.setOrderTitleVm(orderTitleVm);
        orderViewPagerAadpter.notifyDataSetChanged();
        orderCommonNavigatorAdapter.setOrderTitleVms(orderTitleVm);
        mViewBinding.magicIndicator.getNavigator().notifyDataSetChanged();

        mViewBinding.magicIndicator.post(new Runnable() {
            @Override
            public void run() {
                mTempPosition = 0;
                setTitlteAnim(1.2f);
            }
        });

    }


    private static class OrderCommonNavigatorAdapter extends CommonNavigatorAdapter {
        private List<OrderTitleVm> orderTitleVms;
        private XFunc2<Integer, OrderTitleVm> callback;

        public void setOrderTitleVms(List<OrderTitleVm> orderTitleVms) {
            this.orderTitleVms = orderTitleVms;
        }

        public OrderCommonNavigatorAdapter(XFunc2<Integer, OrderTitleVm> callback) {
            this.callback = callback;
        }

        @Override
        public int getCount() {
            return orderTitleVms == null ? 0 : orderTitleVms.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int index) {
            ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
            colorTransitionPagerTitleView.setNormalColor(ResHelper.getColor(R.color.color_text_color_un_select));
            colorTransitionPagerTitleView.setSelectedColor(ResHelper.getColor(R.color.color_primary));
            colorTransitionPagerTitleView.setTextSize(COMPLEX_UNIT_PX, ResHelper.pt2Px(43));
            OrderTitleVm orderTitleVm = CollectionUtil.getSafe(orderTitleVms, index, null);
            String msg = orderTitleVm == null ? "" : orderTitleVm.getTitle();
            colorTransitionPagerTitleView.setText(msg);
            colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        OrderTitleVm orderTitleVm = CollectionUtil.getSafe(orderTitleVms, index, null);
                        callback.call(index, orderTitleVm);
                    }
                }
            });
            return colorTransitionPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
            linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
            linePagerIndicator.setXOffset(ResHelper.pt2Px(20));
            linePagerIndicator.setColors(ResHelper.getColor(R.color.color_backdrop_primary));
            return linePagerIndicator;
        }
    }

    private static class OrderViewPagerAadpter extends FragmentStatePagerAdapter {
        private List<OrderTitleVm> orderTitleVm;

        public void setOrderTitleVm(List<OrderTitleVm> orderTitleVm) {
            this.orderTitleVm = orderTitleVm;
        }

        public OrderViewPagerAadpter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            OfficeItemFragment orderRecommendItemFragment = OfficeItemFragment.newInstance(orderTitleVm.get(position).getType());
            return orderRecommendItemFragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return orderTitleVm == null ? 0 : orderTitleVm.size();
        }
    }


    private void showLocation() {
        City city = locationVm.getLocationCity();
        if (city != null) {
            city = new LocatedCity(city.getName(), city.getProvince(), city.getCode());
        }
        CityPicker.from(this)
                .setLocatedCity((LocatedCity) city)
                .setHotCities(null)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        Log.i("CITYPICKER", "--------------->" + data.toString());
                        locationVm.setCityLiveData(data);
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

    @Override
    public void location(double latitude, double longitude, String locationCity, String cityCode, String province) {
        ((App) Utils.getApp()).locationService.stop();
        locationVm.findCity(locationCity, new XFunc1<City>() {
            @Override
            public void call(City city) {
                city.setLatitude(latitude);
                city.setLongitude(longitude);
                city.setCityCode(cityCode);

                locationVm.setCityLiveData(city);
                locationVm.setLocationCity(city);
            }
        });


    }
}
