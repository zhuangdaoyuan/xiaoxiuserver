package com.xiuxiu.confinement_nurse.ui.my.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutLearningExperienceHeaderBinding;

public class LearningExperienceHeaderView extends LinearLayout {

    private LayoutLearningExperienceHeaderBinding bind;

    public LearningExperienceHeaderView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public LearningExperienceHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public LearningExperienceHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_learning_experience_header, this);
        bind = LayoutLearningExperienceHeaderBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    private void loadData() {
    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }

}
