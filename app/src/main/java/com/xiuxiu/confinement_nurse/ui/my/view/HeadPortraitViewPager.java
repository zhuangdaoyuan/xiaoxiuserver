package com.xiuxiu.confinement_nurse.ui.my.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.noober.background.drawable.DrawableCreator;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutHeadPortraitBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.ui.my.MyInfoActivity;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HeadPortraitViewPager extends LinearLayout {
    HeadPortraitViewPagerAdapter headPortraitViewPagerAdapter;
    private LayoutHeadPortraitBinding bind;

    public HeadPortraitViewPager(Context context) {
        super(context);
        init(context, null);
    }

    public HeadPortraitViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HeadPortraitViewPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_head_portrait, this);
        bind = LayoutHeadPortraitBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
    }

    private void initattrs(Context context, AttributeSet attrs) {
    }


    private void setListener() {
    }

    private void initViewState() {

    }

    private void initView() {
    }


    public void loadData(List<String> urls) {
        headPortraitViewPagerAdapter = new HeadPortraitViewPagerAdapter();
        headPortraitViewPagerAdapter.setHeadPortraitData(urls);
        bind.vpLayoutHeadPortrait.setAdapter(headPortraitViewPagerAdapter);
        bind.indicator.setupWithViewPager(bind.vpLayoutHeadPortrait);
    }


    private static class HeadPortraitViewPagerAdapter extends PagerAdapter {
        private List<String> mHeadPortraitData;


        public void setHeadPortraitData(List<String> mHeadPortraitData) {
            this.mHeadPortraitData = mHeadPortraitData;
        }


        @Override
        public int getCount() {
            return mHeadPortraitData == null ? 0 : mHeadPortraitData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {//当前滑动到的ViewPager页面
            ImageView imageView = new ImageView(container.getContext());
            String safe = CollectionUtil.getSafe(mHeadPortraitData, position, null);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyInfoActivity.start();
                }
            });
            Drawable build = new DrawableCreator.Builder().setSolidColor(ResHelper.getColor(R.color.color_backdrop_default)).setCornersRadius(ResHelper.pt2Px(23)).build();
            Glide.with(imageView).load(safe).placeholder(build)
                    .error(build).into(imageView);
            container.addView(imageView);
            return imageView;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方固定写法
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {//每次划出当前页面的时候就销毁
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }


}
