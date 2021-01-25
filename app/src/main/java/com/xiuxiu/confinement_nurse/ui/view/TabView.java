package com.xiuxiu.confinement_nurse.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutTabBinding;

import org.jetbrains.annotations.NotNull;

public class TabView extends LinearLayout {

    private LayoutTabBinding bind;
    private String mTextMsg;

    public TabView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_tab, this);
        bind = LayoutTabBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    public void setTitle(String title) {
        bind.tvLayoutTitle.setText(title);
    }

    private void initattrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TabView);
            mTextMsg = (a.getString(R.styleable.TabView_text));
            a.recycle();
        }
    }

    private void loadData() {
        setTitle(mTextMsg);
    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }

}
