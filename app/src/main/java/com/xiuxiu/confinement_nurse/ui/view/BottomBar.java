package com.xiuxiu.confinement_nurse.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutBottomBarBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottomBar extends LinearLayout implements View.OnClickListener {
    private LayoutBottomBarBinding bind;
    private List<WeakReference<TextView>> tvList;
    private List<WeakReference<ImageView>> ivList;

    public BottomBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_bottom_bar, this);
        bind = LayoutBottomBarBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    public void loadData() {
        setViewState(String.valueOf(0));
    }

    private void setListener() {

        bind.llLayoutBottomBarCenter.setOnClickListener(this);
        bind.ivLayoutBottomBarCenter.setOnClickListener(this);
        bind.tvLayoutBottomBarCenter.setOnClickListener(this);

        bind.llLayoutBottomBarNews.setOnClickListener(this);
        bind.ivLayoutBottomBarNews.setOnClickListener(this);
        bind.tvLayoutBottomBarNews.setOnClickListener(this);

        bind.llLayoutBottomBarOrder.setOnClickListener(this);
        bind.ivLayoutBottomBarOrder.setOnClickListener(this);
        bind.tvLayoutBottomBarOrder.setOnClickListener(this);
    }


    private void setViewState(String position) {
        if (onSelectItem != null) {
            onSelectItem.onSelect(Integer.parseInt(position));
        }

        for (int i = 0; i < tvList.size(); i++) {
            WeakReference<TextView> viewWeakReference = tvList.get(i);
            if (viewWeakReference.get() != null) {
                boolean b = TextUtils.equals(String.valueOf(viewWeakReference.get().getTag()), position);
                setTextColor(b, viewWeakReference.get());
            }
            WeakReference<ImageView> viewWeakReference1 = ivList.get(i);
            if (viewWeakReference.get() != null) {
                boolean b = TextUtils.equals(String.valueOf(viewWeakReference.get().getTag()), position);
                viewWeakReference1.get().setSelected(b);
            }
        }
    }

    private void setTextColor(boolean isSelect, TextView textView) {
        textView.setTextColor(ResHelper.getColor(isSelect ? R.color.color_primary : R.color.color_656565_a100));
    }

    private void initViewState() {
        tvList = new ArrayList<>();
        tvList.add(new WeakReference<>(bind.tvLayoutBottomBarOrder));
        tvList.add(new WeakReference<>(bind.tvLayoutBottomBarNews));
        tvList.add(new WeakReference<>(bind.tvLayoutBottomBarCenter));

        ivList = new ArrayList<>();
        ivList.add(new WeakReference<>(bind.ivLayoutBottomBarOrder));
        ivList.add(new WeakReference<>(bind.ivLayoutBottomBarNews));
        ivList.add(new WeakReference<>(bind.ivLayoutBottomBarCenter));
    }

    private void initView() {
    }

    private OnSelectItem onSelectItem;

    public void setOnSelectItem(OnSelectItem onSelectItem) {
        this.onSelectItem = onSelectItem;
    }

    @Override
    public void onClick(View v) {
        setViewState(String.valueOf(v.getTag()));
    }

    public interface OnSelectItem {
        public void onSelect(int position);
    }
    public void setFistText(String msg){
        bind.tvLayoutBottomBarOrder.setText(msg);
    }
}
