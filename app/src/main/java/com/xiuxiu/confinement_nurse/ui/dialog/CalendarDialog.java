package com.xiuxiu.confinement_nurse.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.ui.schedule.vm.ScheduleVm;

import java.util.List;

public class CalendarDialog extends BottomPopupView {
    ScheduleVm scheduleVm;
    ISelectCalendar iSelectCalendar;
    public CalendarDialog(@NonNull Context context, ScheduleVm scheduleVm,ISelectCalendar iSelectCalendar) {
        super(context);
        this.scheduleVm = scheduleVm;
        this.iSelectCalendar=iSelectCalendar;
    }


    //
    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_calendar;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView textView=findViewById(R.id.tv_layout_calendar_switch_date);
        CalendarView calendarView = findViewById(R.id.calendarView);
        textView.setText(scheduleVm.getYear() + "/" + scheduleVm.getMonth());


        List<ScheduleVm> scheduleVms = scheduleVm.getScheduleVms();
        if (scheduleVms != null) {
            int size = scheduleVms.size();
            for (int i = 0; i < size; i++) {
                ScheduleVm scheduleVm1 = scheduleVms.get(i);
                Calendar schemeCalendar = getSchemeCalendar(scheduleVm1.getYear(), scheduleVm1.getMonth(), scheduleVm1.getDay());
                calendarView.putMultiSelect(schemeCalendar);
            }
        }

        findViewById(R.id.dialog_calendar_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Calendar> multiSelectCalendars = calendarView.getMultiSelectCalendars();
                if (iSelectCalendar!=null) {
                    iSelectCalendar.selectCalendar(multiSelectCalendars);
                    dismiss();
                }
            }
        });

        calendarView.scrollToCalendar(scheduleVm.getYear(), scheduleVm.getMonth(), 1);
    }

    // 设置最大宽度，看需要而定
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }

    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return ResHelper.pt2Px(1000);
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
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    @Override
    protected int getPopupHeight() {
        return ResHelper.pt2Px(1000);
    }


    private Calendar getSchemeCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(R.color.color_primary);//如果单独标记颜色、则会使用这个颜色
        return calendar;
    }

    public interface  ISelectCalendar{
        public void selectCalendar(List<Calendar> multiSelectCalendars);
    }
}
