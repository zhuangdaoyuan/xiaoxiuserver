package com.xiuxiu.confinement_nurse.ui.schedule.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.LayuoutCalendarSwitchBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.ui.schedule.vm.ScheduleVm;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CalendarSwitchView extends LinearLayout implements
        CalendarView.OnCalendarInterceptListener {
    public static final String KEY_TAG = "CalendarSwitchView";
    private LayuoutCalendarSwitchBinding bind;
    private Map<String, Calendar> calendars = new LinkedHashMap<>();

    public CalendarSwitchView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CalendarSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CalendarSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initattrs(context, attrs);
        View inflate = inflate(getContext(), R.layout.layuout_calendar_switch, this);
        bind = LayuoutCalendarSwitchBinding.bind(inflate);
        initView();
        initViewState();
        setListener();
    }

    private void initattrs(Context context, AttributeSet attrs) {
    }

    private XFunc0 xFunc0;

    public void setSelectCallBack(XFunc0 xFunc0) {
        this.xFunc0 = xFunc0;
    }

    public void loadData(@NotNull ScheduleVm scheduleVm) {


        calendars.clear();

        setSelectViewState(false);
        setCalendarViewState(false);


        bind.tvLayoutCalendarSwitchDate.setText(scheduleVm.getYear() + "/" + scheduleVm.getMonth());
        bind.calendarView.scrollToCalendar(scheduleVm.getYear(), scheduleVm.getMonth(), 1);

        List<ScheduleVm> scheduleVms = scheduleVm.getScheduleVms();
        if (scheduleVms != null) {
            int size = scheduleVms.size();

            for (int i = 0; i < size; i++) {
                ScheduleVm scheduleVm1 = scheduleVms.get(i);
                Calendar schemeCalendar = getSchemeCalendar(scheduleVm1.getYear(), scheduleVm1.getMonth(), scheduleVm1.getDay());
                calendars.put(schemeCalendar.toString(), schemeCalendar);
            }
            bind.calendarView.setSchemeDate(calendars);
        } else {
            bind.calendarView.clearSchemeDate();
        }
    }

    private void setListener() {
        bind.tvLayoutCalendarSwitchOptional.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = bind.ivLayoutCalendarSwitchOptional.isSelected();
                setSelectViewState(!selected);
                setCalendarViewState(!selected);
            }
        });
        bind.tvLayoutCalendarSwitchNoOptional.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = bind.ivLayoutCalendarSwitchOptional.isSelected();
                setSelectViewState(!selected);
                setCalendarViewState(!selected);
            }
        });
        bind.calendarView.setOnCalendarInterceptListener(this);
    }

    private void initViewState() {
        post(new Runnable() {
            @Override
            public void run() {
                int childCount = bind.calendarView.getMonthViewPager().getChildCount();
                for (int i = 0; i < childCount; i++) {
                    bind.calendarView.getMonthViewPager().getChildAt(i).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (xFunc0!=null) {
                                xFunc0.call();
                            }
                        }
                    });

                }
            }
        });
    }

    private void initView() {
    }

    public void setTitle(String msg) {
        bind.tvLayoutCalendarSwitchName.setText(msg);
    }


    private void setSelectViewState(boolean select) {
        bind.ivLayoutCalendarSwitchOptional.setSelected(select);
        bind.ivLayoutCalendarSwitchNoOptional.setSelected(!select);
    }

    private void setCalendarViewState(boolean isSelect) {
        if (isSelect) {
//            bind.calendarView.setThemeColor(R.color.color_primary,R.color.color_text_color_white,R.color.color_text_color_white);
        } else {
//            bind.calendarView.setThemeColor(R.color.color_999999_a100,R.color.color_text_color_black,R.color.color_text_color_black);
        }
    }


    public void setSelectRange(ISelectRange iSelectRange) {
    }

    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return false;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {

    }


//
//    @Override
//    public void onCalendarMultiSelect(Calendar calendar, int curSize, int maxSize) {
//        String key = calendar.toString();
//        boolean b = calendars.containsKey(key);
//        if (b) {
//            calendars.remove(key);
//            bind.calendarView.removeSchemeDate(calendar);
//        } else {
//            calendars.put(key, calendar);
//        }
//    }

    public interface ISelectRange {
        public void onCalendarRangeSelect(String startTime, String endTime);
    }

    private Calendar getSchemeCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(R.color.color_primary);//如果单独标记颜色、则会使用这个颜色
        return calendar;
    }

    public void s() {
        List<Calendar> selectCalendarRange = bind.calendarView.getSelectCalendarRange();
        if (selectCalendarRange == null || selectCalendarRange.size() == 0 && calendars.size() == 0) {
            return;
        }
        if (!selectCalendarRange.isEmpty()) {
            for (Calendar c : selectCalendarRange) {
                Calendar schemeCalendar = getSchemeCalendar(c.getYear(), c.getMonth(), c.getDay());
                String s = schemeCalendar.toString();
                calendars.put(s, schemeCalendar);
            }
        }
        bind.calendarView.setSchemeDate(calendars);
    }
}
