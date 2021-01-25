package com.xiuxiu.confinement_nurse.ui.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.noober.background.BackgroundFactory;


public class XLinearLayout extends LinearLayout {




    public XLinearLayout(Context context) {
        super(context);
    }

    public XLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBackgroundFactory(context, attrs);
    }

    public XLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackgroundFactory(context, attrs);
    }


    //////////////////////////////////////////////////////////////////////////


    private void initBackgroundFactory(Context context, AttributeSet attrs) {
        BackgroundFactory.setViewBackground(context, attrs, this);
    }

}
