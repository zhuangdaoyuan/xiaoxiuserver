package com.xiuxiu.confinement_nurse.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutOrderDetailsBinding;

import org.jetbrains.annotations.NotNull;

public class OrderDetailsItemView extends ConstraintLayout {

    private LayoutOrderDetailsBinding bind;
    private String titleLeft1, titleLeft2, titleRight1, titleRight2;

    public OrderDetailsItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public OrderDetailsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public OrderDetailsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_order_details, this);
        bind = LayoutOrderDetailsBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void initattrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.OrderDetailsItemView);
            titleLeft1 = (a.getString(R.styleable.OrderDetailsItemView_text_left_1));
            titleRight1 = (a.getString(R.styleable.OrderDetailsItemView_text_right_1));
            titleLeft2 = (a.getString(R.styleable.OrderDetailsItemView_text_left_2));
            titleRight2 = (a.getString(R.styleable.OrderDetailsItemView_text_right_2));
            a.recycle();
        }
    }

    private void loadData() {
    }

    private void setListener() {
    }

    private void initViewState() {
        setTitleLeft1(titleLeft1);
        setTitleLeft2(titleLeft2);
        setTitleRight1(titleRight1);
        setTitleRight2(titleRight2);
    }

    private void initView() {
    }


    public void setTitleLeft1(String titleLeft1) {
        this.titleLeft1 = titleLeft1;
        bind.layoutOrderDetailsTitleLeft.setText(titleLeft1);
    }

    public void setTitleLeft2(String titleLeft2) {
        this.titleLeft2 = titleLeft2;
        bind.layoutOrderDetailsTitleLeft2.setText(titleLeft2);
    }

    public void setTitleRight1(String titleRight1) {
        this.titleRight1 = titleRight1;
        bind.layoutOrderDetailsTitleRight.setText(titleRight1);
    }

    public void setTitleRight2(String titleRight2) {
        this.titleRight2 = titleRight2;
        bind.layoutOrderDetailsTitleRight2.setText(titleRight2);
    }
}
