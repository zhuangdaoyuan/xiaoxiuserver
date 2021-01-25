package com.xiuxiu.confinement_nurse.ui.order;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityOrderListBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderInfoViewModel;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_NEED_LOGIN;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_MATRON_ORDER_STATUS_COMPLETED;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_MATRON_ORDER_STATUS_PENDING;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_MATRON_ORDER_STATUS_PROCESSING;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_MATRON_ORDER_STATUS_SETTLED;
import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_MATRON_ORDER_STATUS_TO_BE_CONFIRMED;

/**
 * 订单列表
 */
@Route(path = RouterHelper.KEY_ORDER, extras = KEY_NEED_LOGIN)
public class OrderListActivity extends BaseActivity {

    private OrderViewPagerAadpter orderViewPagerAadpter;
    private CommonNavigator commonNavigator;
    private OrderInfoViewModel orderInfoViewModel;
    private boolean isJiGou;
    public static void start(Context context) {
        ARouter.getInstance().build(RouterHelper.KEY_ORDER).navigation();
    }
    public static void start(Context context,String id,String name) {
        ARouter.getInstance().build(RouterHelper.KEY_ORDER)
                .withString("id",id)
                .withString("name",name)
                .navigation();
    }

    public static void startJiGou(Context context,String id,String name) {
        ARouter.getInstance().build(RouterHelper.KEY_ORDER)
                .withString("id",id)
                .withString("name",name)
                .withBoolean("jigou",true)
                .navigation();
    }

    private ActivityOrderListBinding inflate;
    private OrderCommonNavigatorAdapter orderCommonNavigatorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = ActivityOrderListBinding.inflate(LayoutInflater.from(this));
        setContentView(inflate.getRoot());


        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void loadData() {
        //这边填写的是订单状态数据，作为fragment的tag
        List<String> strings = new ArrayList<>();
        strings.add(KEY_MATRON_ORDER_STATUS_TO_BE_CONFIRMED);
        strings.add(KEY_MATRON_ORDER_STATUS_PENDING);
        strings.add(KEY_MATRON_ORDER_STATUS_PROCESSING);
        strings.add(KEY_MATRON_ORDER_STATUS_COMPLETED);
        strings.add(KEY_MATRON_ORDER_STATUS_SETTLED);
        orderCommonNavigatorAdapter.setOrderTitleVms(strings);
        inflate.magicIndicator.getNavigator().notifyDataSetChanged();
        orderViewPagerAadpter.setOrderTitleVm(strings);
        orderViewPagerAadpter.notifyDataSetChanged();
    }

    private void setListener() {
        inflate.flActivityOrderDetailsBack.setOnClickListener(v -> finish());
    }

    private void initViewState() {
        orderViewPagerAadpter = new OrderViewPagerAadpter(getSupportFragmentManager());
        inflate.mainViewpager.setAdapter(orderViewPagerAadpter);
        inflate.mainViewpager.setOffscreenPageLimit(5);
        commonNavigator = new CommonNavigator(this);
        orderCommonNavigatorAdapter = new OrderCommonNavigatorAdapter(new XFunc2<Integer, String>() {
            @Override
            public void call(Integer integer, String orderTitleVm) {
                inflate.mainViewpager.setCurrentItem(integer);
            }
        });
        commonNavigator.setAdapter(orderCommonNavigatorAdapter);
        inflate.magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(inflate.magicIndicator, inflate.mainViewpager);

        commonNavigator.setAdjustMode(true);
    }

    private void initView() {
        orderInfoViewModel= ViewModelProviders.of(this).get(OrderInfoViewModel.class);
        orderInfoViewModel.setJiGou(getIntent().getBooleanExtra("jigou",false));
        orderInfoViewModel.setMatronId(getIntent().getStringExtra("id"));
        if (!TextUtils.isEmpty(orderInfoViewModel.getMatronId())) {
            String name = getIntent().getStringExtra("name");
            if (!TextUtils.isEmpty(name)) {
                inflate.tvActivityDetailsTitle.setText(name+"的订单");
            }
        }
    }


    private static class OrderCommonNavigatorAdapter extends CommonNavigatorAdapter {
        private List<String> orderTitleVms;
        private XFunc2<Integer, String> callback;

        public void setOrderTitleVms(List<String> orderTitleVms) {
            this.orderTitleVms = orderTitleVms;
        }

        public OrderCommonNavigatorAdapter(XFunc2<Integer, String> callback) {
            this.callback = callback;
        }

        @Override
        public int getCount() {
            return orderTitleVms == null ? 0 : orderTitleVms.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int index) {
            ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
            colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#333333"));
            colorTransitionPagerTitleView.setSelectedColor(ResHelper.getColor(R.color.color_primary));
            colorTransitionPagerTitleView.setTextSize(COMPLEX_UNIT_PX, ResHelper.pt2Px(46));
            String orderTitleVm = CollectionUtil.getSafe(orderTitleVms, index, null);
            colorTransitionPagerTitleView.setText(UserHelper.MatronOrderState.get(orderTitleVm));
            colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        String orderTitleVm = CollectionUtil.getSafe(orderTitleVms, index, null);
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
        private List<String> orderTitleVm;

        public void setOrderTitleVm(List<String> orderTitleVm) {
            this.orderTitleVm = orderTitleVm;
        }

        public OrderViewPagerAadpter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return OrderListFragment.newInstance(orderTitleVm.get(position));
        }

        @Override
        public int getCount() {
            return orderTitleVm == null ? 0 : orderTitleVm.size();
        }
    }


}
