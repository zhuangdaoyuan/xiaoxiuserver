package com.xiuxiu.confinement_nurse.ui.service.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutMyServiceBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.ui.service.vm.MyServerInfoVm;
import com.xiuxiu.confinement_nurse.ui.view.SwitchButton;
import com.xiuxiu.confinement_nurse.utlis.BigDecimalUtils;

public class MyServiceView extends LinearLayout {

    private LayoutMyServiceBinding bind;

    public MyServiceView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MyServiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MyServiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_my_service, this);
        bind = LayoutMyServiceBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    public void loadData(MyServerInfoVm myServerInfoVm) {
        String s = UserHelper.myServiceType.get(myServerInfoVm.getServiceType());
        String s2 = UserHelper.timeType.get(myServerInfoVm.getTimeType());
        String s3=TextUtils.equals(myServerInfoVm.getTimeType(),"0")?"":"("+s2+")";
        bind.tvLayoutServiceName.setText(s+s3);

        try {
            if (TextUtils.equals(myServerInfoVm.getServiceType(),"3")) {
                String div = BigDecimalUtils.div(myServerInfoVm.getTotalPrice(), String.valueOf(100), 2);
                bind.tvLayoutServicePrice.setText("￥" + div +"/1天");
            }else if(TextUtils.equals(myServerInfoVm.getServiceType(),"2")){
                String div = BigDecimalUtils.div(myServerInfoVm.getTotalPrice(), String.valueOf(100), 2);

                bind.tvLayoutServicePrice.setText("￥" + div +"/30天");
            }else if(TextUtils.equals(myServerInfoVm.getServiceType(),"1")){
                String div = BigDecimalUtils.div(myServerInfoVm.getTotalPrice(), String.valueOf(100), 2);

                bind.tvLayoutServicePrice.setText("￥" + div +"/26天");
            }
        }catch (Exception e){
            bind.tvLayoutServicePrice.setText("" );
        }


        String specialCare = myServerInfoVm.getSpecialCare();
        String otherSkills = myServerInfoVm.getOtherSkills();
        bind.flLayoutServiceTags.removeAllViews();
        if (!TextUtils.isEmpty(specialCare) || !TextUtils.isEmpty(otherSkills)) {
            if (!TextUtils.isEmpty(specialCare)) {
                String[] split = specialCare.split(",");
                if (split.length != 0) {
                    for (int i = 0; i < split.length; i++) {
                        String s1 = split[i];
                        if (!TextUtils.equals(s1, "0")) {
                            String msg = UserHelper.specialService.get(s1);
                            if (TextUtils.isEmpty(msg)) {
                                continue;
                            }
                            bind.flLayoutServiceTags.addView(createView(msg), 0);
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(otherSkills)) {
                String[] split = otherSkills.split(",");
                if (split.length != 0) {
                    for (int i = 0; i < split.length; i++) {
                        String s1 = split[i];
                        if (!TextUtils.equals(s1, "0")) {
                            String msg = UserHelper.otherSkills.get(s1);
                            if (TextUtils.isEmpty(msg)) {
                                continue;
                            }
                            bind.flLayoutServiceTags.addView(createView(msg), 0);
                        }
                    }
                }
            }
        }
    }

    private void setListener() {
    }

    private void initViewState() {
        setOrientation(VERTICAL);
    }

    private void initView() {
    }

    private SwitchButton createView(String msg) {
        SwitchButton switchButton = new SwitchButton(getContext());
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(ResHelper.pt2Px(259), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = ResHelper.pt2Px(59);
        switchButton.setLayoutParams(params);
        switchButton.setTitleLeft(msg);
        return switchButton;
    }
}
