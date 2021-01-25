package com.xiuxiu.confinement_nurse.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityServiceExperenceItemBinding;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.order.OrderListDetailsContract;
import com.xiuxiu.confinement_nurse.ui.order.OrderListDetailsPresenter;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderBean;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderServiceVm;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 评价详情
 */
public class ServiceExperienceItemActivity   extends AbsBaseActivity implements OrderListDetailsContract.IView {

    private  ActivityServiceExperenceItemBinding inflate;
    private OrderListDetailsPresenter orderListDetailsPresenter;
    public static void start(Context context, OrderServiceVm item ) {
        Intent starter = new Intent(context, ServiceExperienceItemActivity.class);
        starter.putExtra("data",item);
        context.startActivity(starter);
    }
    @Override
    protected View getLayoutView() {
        this.inflate = ActivityServiceExperenceItemBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderListDetailsPresenter=new OrderListDetailsPresenter(this);
        Serializable data = getIntent().getSerializableExtra("data");
        if (data==null) {
            return;
        }
        OrderServiceVm orderServiceVm= (OrderServiceVm) data;
        orderListDetailsPresenter.requestLoadData(orderServiceVm.getOrderId());

        String userCommentImgs = orderServiceVm.getUserCommentImgs();
        String[] split = userCommentImgs.split(",");
        try {
            String safe1 = split[0];
            GlideHelper.loadImage(safe1,inflate.image1);
        }catch (Exception e){

        }
        try {
            String safe1 = split[1];
            GlideHelper.loadImage(safe1,inflate.image2);
        }catch (Exception e){

        }
        try {
            String safe1 = split[2];
            GlideHelper.loadImage(safe1,inflate.image3);
        }catch (Exception e){

        }
        inflate.evaluate.setText(orderServiceVm.getUserComment());
    }

    @Override
    public void onRequestDetails(OrderBean s) {
        inflate.name.setText(s.getUserNickName());
        inflate.subtitle.setText(s.getUserLocationName());
        GlideHelper.loadGardenImage(s.getUserAvatar(),inflate.headPortrait);
    }
}
