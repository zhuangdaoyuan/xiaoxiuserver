package com.xiuxiu.confinement_nurse.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutOrderRecommendBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.OrderHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeBean;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeItemVm;

public class OfficeItemView extends LinearLayout {

    private LayoutOrderRecommendBinding bind;

    public OfficeItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public OfficeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public OfficeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_order_recommend, this);
        bind = LayoutOrderRecommendBinding.bind(inflate);
        initView();
        initViewState();
        setListener();

    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    public void loadData(OfficeBean orderRecommendItemVm) {
        bind.tvItemOrderRecommendTitle.setText(UserHelper.myServiceType.get(orderRecommendItemVm.getServiceType()));
        bind.tvItemOrderRecommendPrice.setText("¥" + UserHelper.orderByPriceRangeType.get(orderRecommendItemVm.getPriceRangeType()));
        String s2 = DateHelper.stampToDate3(DateHelper.dateToStamp(orderRecommendItemVm.getStartTime()));
        String s3 = DateHelper.stampToDate3(DateHelper.dateToStamp(orderRecommendItemVm.getEndTime()));
        bind.tvItemOrderRecommendTime.setText("服务时间:" + s2 + "-" + s3);
        bind.tvItemOrderRecommendUserName.setText(orderRecommendItemVm.getNickName());
        bind.tvItemOrderRecommendLocation.setText(orderRecommendItemVm.getAddress());


        Glide.with(bind.ivItemOrderRecommendUserImg).load(orderRecommendItemVm.getUserAvatar())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.color.color_backdrop_placeholder)
                .error(R.color.color_backdrop_placeholder)
                .into(bind.ivItemOrderRecommendUserImg);

        bind.flItemOrderTags.removeAllViews();
        String s1 = orderRecommendItemVm.getSpecialRequire() ;
        if (s1!=null&&!TextUtils.isEmpty(s1)) {
            String[] split = s1.split(",");
            for (String s:split){
                if (!UserHelper.specialService.containsKey(s)) {
                    continue;
                }
                View textView = addTagView(UserHelper.specialService.get(s));
                bind.flItemOrderTags.addView(textView);
            }
        }
        String s12 =orderRecommendItemVm.getOtherSkill();
        if (s12!=null&&!TextUtils.isEmpty(s12)) {
            String[] split = s12.split(",");
            for (String s:split){
                if (!UserHelper.otherSkills.containsKey(s)) {
                    continue;
                }
                View textView2 = addTagView( UserHelper.otherSkills.get(s));
                bind.flItemOrderTags.addView(textView2);
            }
        }

        String deliveryState = orderRecommendItemVm.getDeliveryState();
        if (TextUtils.isEmpty(deliveryState)) {
            deliveryState = "0";
        }
        String s = UserHelper.orderByDeliveryState.get(deliveryState);
        bind.tvItemOrderRecommendDelivery.setText(s);

         if (OrderHelper.canItBeDelivered(deliveryState)) {
            bind.tvItemOrderRecommendDelivery.setEnabled(true);
            bind.tvItemOrderRecommendDelivery.setText("未投递");
        } else {
            if(OrderHelper.hasItBeenDelivered(deliveryState)){
                bind.tvItemOrderRecommendDelivery.setText("已投递");
            }else{
                bind.tvItemOrderRecommendDelivery.setText("不可投");
            }
            bind.tvItemOrderRecommendDelivery.setEnabled(false);
        }

    }

    private void setListener() {
    }

    private void initViewState() {
    }

    private void initView() {
    }

    private View addTagView(String tag) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_tag_text, null);
        TextView viewById = inflate.findViewById(R.id.tv_layout_tag_text);
        viewById.setText(tag);
        return inflate;
    }


}
