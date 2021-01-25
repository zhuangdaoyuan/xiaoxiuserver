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

/**
 * 确认弹窗
 *
 */
public class ConfirmDialog  extends CenterPopupView {
    private OnConfirmListener onConfirmListener;
    private CharSequence title;
    private String confirm;
    private boolean noCancel;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    public ConfirmDialog setNoCancel(String confirm) {
        this.noCancel = true;
        this.confirm = confirm;  return this;
    }

    public ConfirmDialog setTitleContent(CharSequence title) {
        this.title = title;
        return this;
    }

    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_cofirm;
    }

    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        TextView viewById = findViewById(R.id.iv_layout_news_user_type_area);
        viewById.setText(title);


        View viewById1 = findViewById(R.id.dialog_text1);
        viewById1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 关闭弹窗
            }
        });

        TextView viewById2 = findViewById(R.id.dialog_text2);
        viewById2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
                }
                dismiss(); // 关闭弹窗
            }
        });

        if (noCancel) {
            ViewHelper.hideView(viewById1);
            viewById2.setText(confirm);
        }
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
        return ResHelper.pt2Px(720);
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    @Override
    protected int getPopupHeight() {
        return ResHelper.pt2Px(460);
    }
}
