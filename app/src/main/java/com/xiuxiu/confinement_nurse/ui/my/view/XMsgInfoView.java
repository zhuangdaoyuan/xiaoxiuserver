package com.xiuxiu.confinement_nurse.ui.my.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutMsgInfoBinding;

import org.jetbrains.annotations.NotNull;

public class XMsgInfoView extends FrameLayout {

    private LayoutMsgInfoBinding bind;
    private String titleLeft1, titleLeft2, titleRight1, titleRight2;
    public XMsgInfoView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public XMsgInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public XMsgInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_msg_info, this);
        bind = LayoutMsgInfoBinding.bind(inflate);

        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void initattrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.XMsgInfoView);
            titleLeft1 = (a.getString(R.styleable.XMsgInfoView_text_left_1));
            titleRight1 = (a.getString(R.styleable.XMsgInfoView_text_right_1));
            titleLeft2 = (a.getString(R.styleable.XMsgInfoView_text_left_2));
            a.recycle();
        }
    }

    private void loadData() {
        setTitleLeft1(titleLeft1);
        setTitleLeft2(titleLeft2);
        setTitleRight1(titleRight2);
    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }
    public void setTitleLeft1(String titleLeft1) {
        this.titleLeft1 = titleLeft1;
        bind.text1.setText(titleLeft1);
    }

    public void setTitleLeft2(String titleLeft2) {
        this.titleLeft2 = titleLeft2;
        bind.text2.setText(titleLeft2);
    }

    public void setTitleRight1(String titleRight1) {
        this.titleRight1 = titleRight1;
        bind.text3.setText(titleRight1);
    }
}
