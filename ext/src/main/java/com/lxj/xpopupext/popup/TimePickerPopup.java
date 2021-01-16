package com.lxj.xpopupext.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.contrarywind.view.WheelView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopupext.R;
import com.lxj.xpopupext.listener.ISelectTimeCallback;
import com.lxj.xpopupext.listener.TimePickerListener;
import com.lxj.xpopupext.view.WheelTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class TimePickerPopup extends BottomPopupView {

    public enum Mode{
        YMDHMS, YMDHM, YMDH, YMD, YM, Y
    }
    public TimePickerListener timePickerListener;
    private Mode mode = Mode.YMD;
    private boolean isLunar = false; //是否是农历
    private int startYear = 0; 
    private int endYear = 0; 
    private int itemsVisibleCount = 7;
    private int itemTextSize = 18;
    private Calendar date = Calendar.getInstance();
    private Calendar startDate,endDate;
    public int dividerColor = 0xFFd5d5d5; //分割线的颜色
    public float lineSpace = 2.4f; // 条目间距倍数 默认2
    public int textColorOut = 0xFFa8a8a8; //分割线以外的文字颜色
    public int textColorCenter = 0xFF2a2a2a; //分割线之间的文字颜色
    public TimePickerPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_ext_time_picker;
    }
    private WheelTime wheelTime; //自定义控件

    public boolean[] mode2type(){
        switch (mode){
            case Y:
                return new boolean[]{true, false, false, false, false, false};
            case YM:
                return new boolean[]{true, true, false, false, false, false};
            case YMD:
                return new boolean[]{true, true, true, false, false, false};
            case YMDH:
                return new boolean[]{true, true, true, true, false, false};
            case YMDHM:
                return new boolean[]{true, true, true, true, true, false};
            default:
                return new boolean[]{true, true, true, true, true, true};
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setTextColor(XPopup.getPrimaryColor());
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePickerListener != null) {
                    try {
                        Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                        timePickerListener.onTimeConfirm(date, v);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dismiss();
            }
        });
        initWheelTime((LinearLayout) findViewById(R.id.timepicker));
    }

    private void initWheelTime(LinearLayout timePickerView) {
        wheelTime = new WheelTime(timePickerView, mode2type(), Gravity.CENTER, itemTextSize);
        if (timePickerListener != null) {
            wheelTime.setSelectChangeCallback(new ISelectTimeCallback() {
                @Override
                public void onTimeSelectChanged() {
                    try {
                        Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                        timePickerListener.onTimeChanged(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        wheelTime.setLunarMode(isLunar);

        if (startYear != 0 && endYear != 0
                && startYear <= endYear) {
            applyYear();
        }

        //若手动设置了时间范围限制
        if (this.startDate != null && this.endDate != null) {
            if (this.startDate.getTimeInMillis() > this.endDate.getTimeInMillis()) {
                throw new IllegalArgumentException("startDate can't be later than endDate");
            } else {
                applyDateRange();
            }
        } else if (this.startDate != null) {
            if (this.startDate.get(Calendar.YEAR) < 1900) {
                throw new IllegalArgumentException("The startDate can not as early as 1900");
            } else {
                applyDateRange();
            }
        } else if (this.endDate != null) {
            if (this.endDate.get(Calendar.YEAR) > 2100) {
                throw new IllegalArgumentException("The endDate should not be later than 2100");
            } else {
                applyDateRange();
            }
        } else {//没有设置时间范围限制，则会使用默认范围。
            applyDateRange();
        }

        setTime();
        wheelTime.setLabels("年", "月", "日", "时", "分", "秒");
        wheelTime.setItemsVisible(itemsVisibleCount);
        wheelTime.setAlphaGradient(true);
        wheelTime.setCyclic(true);
        wheelTime.setDividerColor(dividerColor);
        wheelTime.setDividerType(WheelView.DividerType.FILL);
        wheelTime.setLineSpacingMultiplier(lineSpace);
        wheelTime.setTextColorOut(textColorOut);
        wheelTime.setTextColorCenter(textColorCenter);
        wheelTime.isCenterLabel(false);
    }

    public TimePickerPopup setTimePickerListener(TimePickerListener listener){
        this.timePickerListener = listener;
        return this;
    }
    public TimePickerPopup setItemTextSize(int textSize){
        this.itemTextSize = textSize;
        return this;
    }

    public TimePickerPopup setMode(Mode mode){
        this.mode = mode;
        return this;
    }

    /**
     * 是否是农历
     * @param isLunar
     * @return
     */
    public TimePickerPopup setLunar(boolean isLunar){
        this.isLunar = isLunar;
        return this;
    }

    public TimePickerPopup setItemsVisibleCount(int itemsVisibleCount){
        this.itemsVisibleCount = itemsVisibleCount;
        return this;
    }
    public TimePickerPopup setLineSpace(float lineSpace){
        this.lineSpace = lineSpace;
        return this;
    }
    /**
     * 设置默认时间
     */
    public TimePickerPopup setDefaultDate(Calendar date) {
        this.date = date;
        return this;
    }

    /**
     * 设置年份范围
     */
    public TimePickerPopup setYearRange(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
        return this;
    }

    /**
     * 设置可以选择的时间范围
     */
    public TimePickerPopup setDateRang(Calendar startDate, Calendar endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    private void applyYear(){
        wheelTime.setStartYear(this.startYear);
        wheelTime.setEndYear(this.endYear);
    }
    private void applyDateRange(){
        wheelTime.setRangDate(this.startDate, this.endDate);
        initDefaultSelectedDate();
    }

    private void initDefaultSelectedDate() {
        //如果手动设置了时间范围
        if (this.startDate != null && this.endDate != null) {
            //若默认时间未设置，或者设置的默认时间越界了，则设置默认选中时间为开始时间。
            if (this.date == null || this.date.getTimeInMillis() < this.startDate.getTimeInMillis()
                    || this.date.getTimeInMillis() > this.endDate.getTimeInMillis()) {
                this.date = this.startDate;
            }
        } else if (this.startDate != null) {
            //没有设置默认选中时间,那就拿开始时间当默认时间
            this.date = this.startDate;
        } else if (this.endDate != null) {
            this.date = this.endDate;
        }
    }

    /**
     * 设置选中时间,默认选中当前时间
     */
    private void setTime() {
        int year, month, day, hours, minute, seconds;
        Calendar calendar = Calendar.getInstance();

        if (this.date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
        } else {
            year = this.date.get(Calendar.YEAR);
            month = this.date.get(Calendar.MONTH);
            day = this.date.get(Calendar.DAY_OF_MONTH);
            hours = this.date.get(Calendar.HOUR_OF_DAY);
            minute = this.date.get(Calendar.MINUTE);
            seconds = this.date.get(Calendar.SECOND);
        }
        wheelTime.setPicker(year, month, day, hours, minute, seconds);
    }

}
