package com.xiuxiu.confinement_nurse.ui.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.noober.background.BackgroundFactory;


public class XFrameLayout extends FrameLayout {


    private Runnable runnableMarquee;


    public XFrameLayout(Context context) {
        super(context);
    }

    public XFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBackgroundFactory(context, attrs);
    }

    public XFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackgroundFactory(context, attrs);
    }


    //////////////////////////////////////////////////////////////////////////


    private void initBackgroundFactory(Context context, AttributeSet attrs) {
        BackgroundFactory.setViewBackground(context, attrs, this);
    }

}
