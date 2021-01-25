package com.xiuxiu.confinement_nurse.ui.my.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.my.vm.OtherExperienceVm;

public class OtherExperienceBottomView extends FrameLayout {
    public OtherExperienceBottomView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public OtherExperienceBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public OtherExperienceBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        inflate(getContext(), R.layout.layout_other_experience_bottom, this);
        initView();
        initViewState();
        setListener();
    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    public void loadData(OtherExperienceVm orderRecommendItemVm) {
    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }

}
