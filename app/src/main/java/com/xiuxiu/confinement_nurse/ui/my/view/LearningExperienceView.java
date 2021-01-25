package com.xiuxiu.confinement_nurse.ui.my.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;

import java.text.ParseException;

public class LearningExperienceView extends LinearLayout {

    private com.xiuxiu.confinement_nurse.databinding.LayoutLearningExperienceBinding bind;

    public LearningExperienceView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public LearningExperienceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public LearningExperienceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_learning_experience, this);
        bind = com.xiuxiu.confinement_nurse.databinding.LayoutLearningExperienceBinding.bind(inflate);
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


    public void setTextSubject(String text) {
        bind.tvLayoutLearningSubject.setText(text);

    }

    public void setTextLevel(String text) {
        bind.tvLayoutLearningLevel.setText(text);
    }

    public void setTextSchool(String text) {
        bind.tvLayoutLearningSchool.setText(text);
    }

    public void setTextTime(String time) {
        bind.tvLayoutLearningTime.setText(time);
    }

    public void setImage(String url) {
        Glide.with(this).load(url).into(bind.ivLayoutLearningPhoto);
    }

    public void loadData(LearningExperienceVm.LearningExperience orderRecommendItemVm) {

        if (TextUtils.equals(orderRecommendItemVm.getType(), "1")) {
            bind.tvLayoutLearningSubject.setText(UserHelper.serviceType.get(orderRecommendItemVm.getSubject()));
            bind.tvLayoutLearningLevel.setText(UserHelper.levelType.get(orderRecommendItemVm.getLevel()));
        } else {
            bind.tvLayoutLearningSubject.setText(UserHelper.educationType.get(orderRecommendItemVm.getDegree()));
            bind.tvLayoutLearningLevel.setText("");
        }

        bind.tvLayoutLearningSchool.setText(orderRecommendItemVm.getSchool());


        String s = DateHelper.stampToDate3(DateHelper.dateToStamp(orderRecommendItemVm.getStartTime()));
        bind.tvLayoutLearningTime.setText(s);

        GlideHelper.loadImage(orderRecommendItemVm.getImage(), bind.ivLayoutLearningPhoto);
    }

}
