package com.xiuxiu.confinement_nurse.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class SuccessfulDeliveryDialog extends CenterPopupView {

    public SuccessfulDeliveryDialog(@NonNull Context context) {
        super(context);
    }


    //
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_successful_delivery;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        dismiss();
                    }
                });
    }

    // 设置最大宽度，看需要而定
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }

    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }

    // 设置自定义动画器，看需要而定
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    @Override
    protected int getPopupWidth() {
        return ResHelper.pt2Px(253);
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    @Override
    protected int getPopupHeight() {
        return ResHelper.pt2Px(253);
    }
}
