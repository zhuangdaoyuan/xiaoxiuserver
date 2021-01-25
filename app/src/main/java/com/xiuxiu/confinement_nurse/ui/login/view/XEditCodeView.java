package com.xiuxiu.confinement_nurse.ui.login.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.noober.background.drawable.DrawableCreator;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayoutEditCodeViewBinding;
import com.xiuxiu.confinement_nurse.databinding.LayoutEditViewBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;

/**
 * 输入编辑框 验证码
 */
public class XEditCodeView extends LinearLayout implements TextWatcher {

    private LayoutEditCodeViewBinding bind;
    private String title;
    private int iconLeft;
    private String titleHint;
    private String mTextRight;

    public XEditCodeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public XEditCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public XEditCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layout_edit_code_view, this);
        bind = LayoutEditCodeViewBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void initattrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.XEditCodeView);
            title = (a.getString(R.styleable.XEditCodeView_text));
            titleHint = (a.getString(R.styleable.XEditCodeView_text_hint));
            iconLeft = (a.getResourceId(R.styleable.XEditCodeView_icon_left, 0));
            mTextRight = a.getString(R.styleable.XEditCodeView_text_right);

            a.recycle();
        }
    }

    private void loadData() {
        setText(title);
        setTextHint(titleHint);
        setIconLeft(iconLeft);
        setTextRight(mTextRight);
    }

    private void setListener() {

        bind.ivLayoutEditLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickLeft();
                }
            }
        });
        bind.tvLayoutEditCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickRight();
                }
            }
        });
        bind.tvLayoutEditText.addTextChangedListener(this);
    }

    private void initViewState() {
        setOrientation(HORIZONTAL);

        Drawable build = new DrawableCreator.Builder().setCornersRadius(ResHelper.pt2Px(63))
                .setSolidColor(Color.parseColor("#E6E6E6")).build();
        setBackground(build);
    }

    private void initView() {
    }

    public EditText getEditText() {
        return bind.tvLayoutEditText;
    }

    public void setTextHint(String textHint) {
        bind.tvLayoutEditText.setHint(textHint);
    }

    public void setText(String text) {
        bind.tvLayoutEditText.setText(text);
    }

    public void setTextRight(String text) {
        bind.tvLayoutEditCode.setText(text);
    }

    public void setIconLeft(int image) {
        bind.ivLayoutEditLeftIcon.setImageDrawable(ResHelper.getDrawable(image));
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (addTextChangedListener != null) {
            addTextChangedListener.onTextChange();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getTitle() {
        return bind.tvLayoutEditText.getText().toString();
    }


    private addTextChangedListener addTextChangedListener;
    private OnClickListener onClickListener;

    public void setOnImageClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setAddTextChangedListener(addTextChangedListener addTextChangedListener) {
        this.addTextChangedListener = addTextChangedListener;
    }

    public interface addTextChangedListener {
        void onTextChange();
    }


    public interface OnClickListener {
        void onClickLeft();

        void onClickRight();
    }


}
