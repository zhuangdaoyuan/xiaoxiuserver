package com.xiuxiu.confinement_nurse.ui.my.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutMsgInfoBinding;
import com.xiuxiu.confinement_nurse.databinding.LayoutMyServiceMsgInfoBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;

public class MyServiceMsgInfoView extends FrameLayout {

    private LayoutMyServiceMsgInfoBinding bind;
    private String titleLeft1, titleLeft2, titleRightHint, titleRight2;
    private boolean showRightIcon;
    private boolean isopen;

    public MyServiceMsgInfoView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MyServiceMsgInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MyServiceMsgInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_my_service_msg_info, this);
        bind = LayoutMyServiceMsgInfoBinding.bind(inflate);

        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void initattrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyServiceMsgInfoView);
            titleLeft1 = (a.getString(R.styleable.MyServiceMsgInfoView_text_left_1));
            titleLeft2 = (a.getString(R.styleable.MyServiceMsgInfoView_text_left_2));
            titleRightHint = (a.getString(R.styleable.MyServiceMsgInfoView_text_right_hint));

            showRightIcon = (a.getBoolean(R.styleable.MyServiceMsgInfoView_image_show, true));

            a.recycle();
        }
    }

    public void setShowRightIcon(boolean showRightIcon) {
        ViewHelper.showViewByFocus(bind.text3, showRightIcon);
        setTitleLeft1(titleLeft1);
        setTitleLeft2(titleLeft2);
        setTitleRightHint(titleRightHint);
    }

    private void loadData() {
    }

    private void setListener() {
    }

    private void initViewState() {
        setShowRightIcon(showRightIcon);
    }

    private void initView() {
    }

    public void setTitleRightHint(String title) {
        bind.text4.setHint(title);
    }

    public void setTitleLeft1(String titleLeft1) {
        this.titleLeft1 = titleLeft1;
        bind.text1.setText(titleLeft1);
    }

    public void setTitleLeft2(String titleLeft2) {
        this.titleLeft2 = titleLeft2;
        bind.text2.setText(titleLeft2);
    }

    public void setSubTitleRight(String title) {
        bind.text4.setText(title);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onClick(v);
                }
                isopen = !isopen;
                changeImage(isopen);
            }
        });
    }

    public void setOpenState(boolean isOpen) {
        isopen = isOpen;
        changeImage(isopen);
    }

    private void changeImage(boolean isOpen) {
        if (isOpen) {
            bind.text3.setBackground(ResHelper.getDrawable(R.drawable.icon_up));
        } else {
            bind.text3.setBackground(ResHelper.getDrawable(R.drawable.icon_down));
        }
    }
}
