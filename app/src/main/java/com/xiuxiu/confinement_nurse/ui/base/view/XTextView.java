package com.xiuxiu.confinement_nurse.ui.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.noober.background.BackgroundFactory;


public class XTextView extends TextView {


    private Runnable runnableMarquee;


    public XTextView(Context context) {
        super(context);
    }

    public XTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBackgroundFactory(context, attrs);
    }

    public XTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackgroundFactory(context, attrs);
    }


    //////////////////////////////////////////////////////////////////////////


    private void initBackgroundFactory(Context context, AttributeSet attrs) {
        BackgroundFactory.setViewBackground(context, attrs, this);
    }

}
