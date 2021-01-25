package com.xiuxiu.confinement_nurse.ui.my.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutServiceExperienceBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderServiceVm;

import java.text.ParseException;

public class ServiceExperienceView extends FrameLayout {

    private LayoutServiceExperienceBinding bind;

    public ServiceExperienceView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ServiceExperienceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ServiceExperienceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_service_experience, this);
        bind = LayoutServiceExperienceBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    public void loadData(OrderServiceVm orderRecommendItemVm) {
        String serviceType = orderRecommendItemVm.getServiceType();
        if (TextUtils.equals(serviceType, "1")) {
            bind.tvLayoutServiceExperienceService.setText("月嫂服务");
        } else if (TextUtils.equals(serviceType, "2")) {
            bind.tvLayoutServiceExperienceService.setText("育婴师服务");
        } else {
            bind.tvLayoutServiceExperienceService.setText("其他服务");
        }

        String serviceStartTime = orderRecommendItemVm.getServiceStartTime();
        String s = DateHelper.stampToDate2(DateHelper.dateToStamp(serviceStartTime));

        String serviceEndTime = orderRecommendItemVm.getServiceEndTime();
        String s1 = DateHelper.stampToDate2(DateHelper.dateToStamp(serviceEndTime));
        bind.tvLayoutServiceExperienceTime.setText(s + "至" + s1);

        bind.tvLayoutServiceExperienceEvaluate.setText(orderRecommendItemVm.getUserComment());

        bind.tvLayoutServiceExperienceArea.setText(orderRecommendItemVm.getLocation());

    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }

}
