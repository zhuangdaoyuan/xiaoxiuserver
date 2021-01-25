package com.xiuxiu.confinement_nurse.ui.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.noober.background.BackgroundFactory;


public class XButton extends Button {


    private Runnable runnableMarquee;


    public XButton(Context context) {
        super(context);
    }

    public XButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBackgroundFactory(context, attrs);
    }

    public XButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackgroundFactory(context, attrs);
    }


    //////////////////////////////////////////////////////////////////////////


    private void initBackgroundFactory(Context context, AttributeSet attrs) {
        BackgroundFactory.setViewBackground(context, attrs, this);
    }

}
