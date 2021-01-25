package com.xiuxiu.confinement_nurse.ui.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.noober.background.BackgroundFactory;


public class XEditTextView extends EditText {


    private Runnable runnableMarquee;


    public XEditTextView(Context context) {
        super(context);
    }

    public XEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBackgroundFactory(context, attrs);
    }

    public XEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackgroundFactory(context, attrs);
    }


    //////////////////////////////////////////////////////////////////////////


    private void initBackgroundFactory(Context context, AttributeSet attrs) {
        BackgroundFactory.setViewBackground(context, attrs, this);
    }

}
