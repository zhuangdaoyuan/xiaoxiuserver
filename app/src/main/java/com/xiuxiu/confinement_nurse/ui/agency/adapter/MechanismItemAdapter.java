package com.xiuxiu.confinement_nurse.ui.agency.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.ui.service.vm.MyServerInfoVm;
import com.xiuxiu.confinement_nurse.utlis.BigDecimalUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.xiuxiu.confinement_nurse.help.UserHelper.myServiceType;
import static com.xiuxiu.confinement_nurse.help.UserHelper.myServiceType2;

public class MechanismItemAdapter extends BaseQuickAdapter<UserInfoBean, BaseViewHolder> implements LoadMoreModule {

    public MechanismItemAdapter() {
        super(R.layout.item_yuesao);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, UserInfoBean orderRecommendItemVm) {
        String userName = orderRecommendItemVm.getUserName();
        if (TextUtils.isEmpty(userName)) {
            userName = "没有填写";
        }
        baseViewHolder.setText(R.id.tv_item_order_recommend_namne, userName);
        baseViewHolder.setText(R.id.tv_item_order_recommend_age, orderRecommendItemVm.getAge() + "岁");

        GlideHelper.loadImage(orderRecommendItemVm.getAvatarList(), baseViewHolder.getView(R.id.item_yuesao_avatar));

    //////标签///////////////////////
        List<String> educationList = orderRecommendItemVm.getEducationList();
//        educationList=new ArrayList<String>(){{
//            add("sssssss");
//            add("sssssss");
//            add("sssssss");
//        }};

        LinearLayout view = baseViewHolder.getView(R.id.item_yuesao_content);
        if (educationList == null || educationList.size() == 0) {
            ViewHelper.hideView(view);
        } else {
            ViewHelper.showView(view);
            view.removeAllViews();
            for (int i = 0; i < educationList.size(); i++) {
                String s = educationList.get(i);
                view.addView(createTextView(view.getContext(), s,i));
            }
        }

////////////////价格///////////////////////////////
        List<MyServerInfoVm> matronServiceList = orderRecommendItemVm.getMatronServiceList();
        if (matronServiceList != null) {
            int size = matronServiceList.size();
            if (size >0) {
                MyServerInfoVm myServerInfoVm = matronServiceList.get(size-1);
                baseViewHolder.setText(R.id.tv_item_order_recommend_price, "¥" + BigDecimalUtils.div(myServerInfoVm.getTotalPrice(), String.valueOf(100), 2));
                baseViewHolder.setText(R.id.tv_item_order_recommend_price2, "/"+myServerInfoVm.getDays() + "天");


                String workYears = orderRecommendItemVm.getWorkYears();
                StringBuffer stringBuffer=new StringBuffer();
                String serviceType=myServerInfoVm.getServiceType();
                String serviceMsg=myServiceType2.get(serviceType);
                if (TextUtils.isEmpty(serviceMsg)) {
                    serviceMsg="不限";
                }
                stringBuffer.append(serviceMsg);
                stringBuffer.append("|");
                stringBuffer.append(workYears).append("年");
                stringBuffer.append("|");
                stringBuffer.append(orderRecommendItemVm.getNativePlaceName());

                baseViewHolder.setText(R.id.tv_item_order_recommend_info, stringBuffer.toString());

            }
        }else{
            baseViewHolder.setText(R.id.tv_item_order_recommend_price, "");
            baseViewHolder.setText(R.id.tv_item_order_recommend_price2, "");
        }
    }

    private TextView createTextView(Context context, String msg,int position) {
        TextView textView = new TextView(context);
        textView.setBackgroundColor(Color.parseColor("#EFEFEF"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.pt2Px(32));
        textView.setText(msg);
        LinearLayout.LayoutParams s=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        s.setMargins(position==0?0:ResHelper.pt2Px(9),0,0,0);
        textView.setLayoutParams(s);
        int i = ResHelper.pt2Px(8);
        textView.setPadding(i, i, i, i);
        return textView;
    }
}
