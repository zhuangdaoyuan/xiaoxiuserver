package com.xiuxiu.confinement_nurse.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.xiuxiu.confinement_nurse.Constants;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.ui.agency.AgencyMainActivity;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.view.XButton;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;


public class GuideActivity extends BaseActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewpager;
    private XButton login;
    private LinearLayout llContent;

    public static void start(Context context) {
        Intent starter = new Intent(context, GuideActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void loadData() {
    }

    private void setListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeView(0);
                changeView(1);
                changeView(2);
                if (position == 2) {
                    ViewHelper.showView(login);
                } else {
                    ViewHelper.hideView(login);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeView(int position) {
        View childAt = llContent.getChildAt(position);
        childAt.setSelected(viewpager.getCurrentItem() == position);
    }

    private void initViewState() {
        viewPagerAdapter = new ViewPagerAdapter();
        viewpager.setAdapter(viewPagerAdapter);
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        login = (XButton) findViewById(R.id.login);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
    }

    public void onMain(View view) {
//        if (ModelManager.getInstance().getCacheInterface().showProtocol()) {
//            DialogHelper.showProtocolDialog(view.getContext(), new XFunc0() {
//                @Override
//                public void call() {
//                    ModelManager.getInstance().getCacheInterface().saveProtocol();
//                    ModelManager.getInstance().getCacheInterface().saveGuide();
//                    MainActivity.start(view.getContext());
//                    finish();
//                }
//            });
//        } else {
//            MainActivity.start(view.getContext());
//        }
        ModelManager.getInstance().getCacheInterface().saveProtocol();
       ModelManager.getInstance().getCacheInterface().saveGuide();
        if (UserHelper.isLogin()) {
            UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
            if (TextUtils.equals(userBean.getUserType(), Constants.KEY_AGENCY)) {
                AgencyMainActivity.start(GuideActivity.this);
                finish();
                return;
            }
        }
        MainActivity.start(GuideActivity.this);
        finish();
    }

    public static class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            container.addView(view);
            ViewPager.LayoutParams v = new ViewPager.LayoutParams();
            view.setLayoutParams(v);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            if (position == 0) {
                Glide.with(view).load(R.drawable.icon_one).into(view);
            } else if (position == 1) {
                Glide.with(view).load(R.drawable.icon_two).into(view);
            } else if (position == 2) {
                Glide.with(view).load(R.drawable.icon_three).into(view);
            }
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
