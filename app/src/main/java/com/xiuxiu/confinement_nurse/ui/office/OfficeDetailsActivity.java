package com.xiuxiu.confinement_nurse.ui.office;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityOrderDetailsBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.OrderHelper;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeBean;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeDetailsVm;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_NEED_LOGIN;

/**
 * 订单详情
 */
@Route(path = RouterHelper.KEY_ORDERDETAILSACTIVITY, extras = KEY_NEED_LOGIN)
public class OfficeDetailsActivity extends AbsBaseActivity implements OfficeDetailsContract.IView {

    public static final String KEY_ORDER_ID = "ORDER_ID";

    public static void start(Context context, String orderId) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, OfficeDetailsActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(KEY_ORDER_ID, orderId);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OfficeDetailsContract.IPresenter presenter;
    private ActivityOrderDetailsBinding inflate;
    private String orderId;

    @Override
    protected View getLayoutView() {
        inflate = ActivityOrderDetailsBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected Object getTargetView() {
        return inflate.bg;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        if (initViewState(savedInstanceState)) {
            return;
        }
        setListener();
        loadData();
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.color_F7D1FF_a100;
    }

    private void loadData() {
        presenter.requestOrderDetails(orderId);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_ORDER_ID, orderId);
    }

    private void setListener() {
        inflate.tvActivityOrderDetailsDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestDelivery(orderId);
            }
        });
    }

    private boolean initViewState(Bundle save) {
        if (save != null) {
            orderId = save.getString(KEY_ORDER_ID);
        } else {
            orderId = getIntent().getStringExtra(KEY_ORDER_ID);
        }
        if (TextUtils.isEmpty(orderId)) {
            finish();
            ToastHelper.showToast("数据错误");
            return true;
        }
        presenter = new OfficeDetailsPresenter(this);
        return false;
    }

    private void initView() {
    }

    @Override
    public void onReload(View v) {
        loadData();
    }

    @Override
    public void onRequestOrderDetails(OfficeBean orderDetailsVm) {
        inflate.vActivityOrderDetailsUserName.setText(orderDetailsVm.getNickName());

        inflate.vActivityOrderDetailsArea.setText(orderDetailsVm.getAddress());
//        inflate.tvActivityOrderDetailsArea2.setText(orderDetailsVm.getArea2());


        String s2 = DateHelper.stampToDate3(DateHelper.dateToStamp(orderDetailsVm.getStartTime()));
        String s3 = DateHelper.stampToDate3(DateHelper.dateToStamp(orderDetailsVm.getEndTime()));
        inflate.tvActivityOrderDetailsTime.setText("服务时间:" + s2 + "-" + s3);

        inflate.tvActivityOrderDetailsInfo.setText(UserHelper.serviceType.get(orderDetailsVm.getServiceType()));


        Glide.with(inflate.tvActivityOrderDetailsAvatar).load(orderDetailsVm.getUserAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.color.color_backdrop_placeholder)
                .error(R.color.color_backdrop_placeholder)
                .into(inflate.tvActivityOrderDetailsAvatar);


        inflate.tvActivityOrderDetailsPrice.setText("¥" + UserHelper.orderByPriceRangeType.get(orderDetailsVm.getPriceRangeType()));


        if (OrderHelper.canItBeDelivered(orderDetailsVm.getDeliveryState())) {
            ViewHelper.showView(inflate.tvActivityOrderDetailsDelivery);
            ViewHelper.hideView(inflate.tvActivityOrderDetailsDelivery2);

            inflate.tvActivityOrderDetailsDelivery.setEnabled(!OrderHelper.hasItBeenDelivered(orderDetailsVm.getDeliveryState()));
        } else {
            ViewHelper.showView(inflate.tvActivityOrderDetailsDelivery2);
            ViewHelper.hideView(inflate.tvActivityOrderDetailsDelivery);
        }

        String maxAge = orderDetailsVm.getMaxAge();
        String age;
        if (maxAge.length() > 2) {
            age="不限";
        }else{
            age=orderDetailsVm.getMinAge() + "-" + maxAge + "岁";
        }
        inflate.odvActivityOrderDetailsInfo1.setTitleLeft1("年龄：" + age);

        //学历
        String education = orderDetailsVm.getEducation();
        String s4 = UserHelper.educationType.get(education);
        if (TextUtils.isEmpty(s4)) {
            s4 = "不限";
        }
        inflate.odvActivityOrderDetailsInfo1.setTitleLeft2("学历：" + s4);

        inflate.odvActivityOrderDetailsInfo1.setTitleRight1("籍贯:不限");

        //服务年限
        String workYears = orderDetailsVm.getWorkYears();
        try {
            int i = Integer.parseInt(workYears);
            if (i <= 0) {
                workYears = "不限";
            }else{
                workYears=i+"年";
            }
        } catch (NumberFormatException e) {
        }

        inflate.odvActivityOrderDetailsInfo1.setTitleRight2("服务年限：" + workYears );

        StringBuffer stringBuffer = new StringBuffer();
        String s1 = orderDetailsVm.getSpecialRequire();
        if (s1 != null && !TextUtils.isEmpty(s1)) {
            String[] split = s1.split(",");
            for (String s : split) {
                if (stringBuffer.length() != 0) {
                    stringBuffer.append("/").append(UserHelper.specialService.get(s));
                } else {
                    stringBuffer.append(UserHelper.specialService.get(s));
                }
            }
        } else {
            stringBuffer.append("无");
        }
        inflate.odvActivityOrderDetailsInfo2.setTitleLeft1("特殊要求：" + stringBuffer.toString());


        StringBuffer stringBuffer2 = new StringBuffer();
        String s12 = orderDetailsVm.getOtherSkill();
        if (s12 != null && !TextUtils.isEmpty(s12)) {
            String[] split = s12.split(",");
            for (String s : split) {
                if (stringBuffer2.length() == 0) {
                    stringBuffer2.append(UserHelper.otherSkills.get(s));
                } else {
                    stringBuffer2.append("/").append(UserHelper.otherSkills.get(s));
                }
            }
        } else {
            stringBuffer.append("不限");
        }
        inflate.odvActivityOrderDetailsInfo2.setTitleRight1("技能：" + stringBuffer2);


        inflate.odvActivityOrderDetailsInfo2.setTitleLeft2("态度:耐心");
        inflate.odvActivityOrderDetailsInfo2.setTitleRight2("其他:无");


        //备注

        inflate.odvActivityOrderDetailsInfo3.setTitleLeft1("备注:"+orderDetailsVm.getRemark());
    }

    @Override
    public void onRequestDelivery() {
        DialogHelper.showSuccessfulDeliveryDialog(this);
        if (OrderHelper.canItBeDelivered("1")) {
            ViewHelper.showView(inflate.tvActivityOrderDetailsDelivery);
            ViewHelper.hideView(inflate.tvActivityOrderDetailsDelivery2);

            inflate.tvActivityOrderDetailsDelivery.setEnabled(!OrderHelper.hasItBeenDelivered("1"));
        } else {
            ViewHelper.showView(inflate.tvActivityOrderDetailsDelivery2);
            ViewHelper.hideView(inflate.tvActivityOrderDetailsDelivery);
        }
    }
}
