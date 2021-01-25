package com.xiuxiu.confinement_nurse.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutButtonBinding;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.ui.base.view.XButton;
import com.xiuxiu.confinement_nurse.ui.base.view.XFrameLayout;

import org.jetbrains.annotations.NotNull;

public class SwitchButton extends XFrameLayout {
    private String titleLeft;
    private LayoutButtonBinding bind;

    public SwitchButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_button, this);
        bind = LayoutButtonBinding.bind(inflate);

        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void initattrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton);
            titleLeft = (a.getString(R.styleable.SwitchButton_text_left_1));
            a.recycle();
        }
    }

    private void loadData() {
        setTitleLeft(titleLeft);
    }

    private void setListener() {
//        bind.layoutButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean selected = bind.layoutButton.isSelected();
//                boolean selected1 = !selected;
//                setButtonSelectState(selected1);
//            }
//        });
    }

    public void setButtonSelectState(boolean selected) {
        bind.layoutButton.setSelected(selected);
        if (selected) {
            ViewHelper.showView(bind.layoutImage);
        } else {
            ViewHelper.hideView(bind.layoutImage);
        }
    }

    public boolean isSelect() {
        return bind.layoutButton.isSelected();
    }

    private void initViewState() {
    }

    private void initView() {
    }

    public void setTitleLeft(String titleLeft) {
        bind.layoutButton.setText(titleLeft);
    }


}
