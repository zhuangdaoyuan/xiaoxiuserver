package com.xiuxiu.confinement_nurse.ui.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.ui.base.view.XTextView;

public class ProtocolDialog extends CenterPopupView {
    private OnConfirmListener onConfirmListener;
    private XTextView agree;
    private XTextView quit;

    public ProtocolDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_protocol;
    }

    public ProtocolDialog setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        quit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHelper.exit();
            }
        });
        agree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
                }
            }
        });
    }

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
        return ResHelper.pt2Px(832);
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    @Override
    protected int getPopupHeight() {
        return ResHelper.pt2Px(1264);
    }

    private void initView() {
        agree = (XTextView) findViewById(R.id.agree);
        quit = (XTextView) findViewById(R.id.quit);
    }
}
