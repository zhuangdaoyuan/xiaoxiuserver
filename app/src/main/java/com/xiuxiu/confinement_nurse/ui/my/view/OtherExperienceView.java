package com.xiuxiu.confinement_nurse.ui.my.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutOtherExperienceBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.ui.my.vm.OtherExperienceVm;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;

public class OtherExperienceView extends FrameLayout {

    private LayoutOtherExperienceBinding bind;

    public OtherExperienceView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public OtherExperienceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public OtherExperienceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_other_experience, this);
        bind = LayoutOtherExperienceBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    public void loadData(OtherExperienceVm.OtherExperience orderRecommendItemVm) {
        String serviceType = orderRecommendItemVm.getServiceType();
        if (TextUtils.equals(serviceType, "1")) {
            bind.tvLayoutOtherExperienceTitle.setText("月嫂服务");
        } else if (TextUtils.equals(serviceType, "2")) {
            bind.tvLayoutOtherExperienceTitle.setText("育婴师服务");
        } else {
            bind.tvLayoutOtherExperienceTitle.setText("其他服务");
        }

        String serviceStartTime = orderRecommendItemVm.getServiceStartTime();
        String s = DateHelper.stampToDate2(DateHelper.dateToStamp(serviceStartTime));

        String serviceEndTime = orderRecommendItemVm.getServiceEndTime();
        String s1 = DateHelper.stampToDate2(DateHelper.dateToStamp(serviceEndTime));
        bind.tvLayoutOtherExperienceTime.setText(s + "至" + s1);


        GlideHelper.loadImage(orderRecommendItemVm.getImages(), bind.ivLayoutOtherExperience);

        String type;
        switch (orderRecommendItemVm.getObjectType()) {
            case "1":
                type = "单胎新生儿";
                break;
            case "2":
                type = "双胎新生儿";
                break;
            case "3":
                type = "零到一岁幼儿";
            case "4":
                type = "一岁以上幼儿";
                break;
            case "0":
            default:
                type = "其他";
                break;
        }
        bind.tvLayoutOtherExperienceType.setText(type);

        String groupType;
        switch (orderRecommendItemVm.getGroupType()) {
            case "1":
                groupType = "个人订单";
                break;
            case "2":
                groupType = "机构订单";
                break;
            case "0":
            default:
                groupType = "其他订单";
                break;
        }
        bind.tvLayoutOtherExperienceMechanism.setText(groupType);

        bind.tvLayoutOtherExperienceArea.setText(orderRecommendItemVm.getLocation());
    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }

}
