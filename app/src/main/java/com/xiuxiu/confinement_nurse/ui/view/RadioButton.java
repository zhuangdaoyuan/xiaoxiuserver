package com.xiuxiu.confinement_nurse.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutRadioBinding;
import com.xiuxiu.confinement_nurse.ui.base.view.XLinearLayout;

public class RadioButton extends XLinearLayout implements View.OnClickListener {

    private  LayoutRadioBinding bind;
    private String text;

    public RadioButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_radio, this);
        bind = LayoutRadioBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void initattrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RadioButton);
            text = (a.getString(R.styleable.RadioButton_text_left));
            a.recycle();
        }
    }

    private void loadData() {
        setText(text);
    }

    private void setListener() {
        setOnClickListener(this);
    }

    private void initViewState() {
    }

    private void initView() {
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        super.dispatchSetSelected(selected);
        bind.ivLayoutRadio.setSelected(selected);
    }

    public void setText(String text){
        bind.tvLayoutRadio.setText(text);
    }

    @Override
    public void onClick(View v) {
        setSelected(!isSelected());
    }
}
