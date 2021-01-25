package com.xiuxiu.confinement_nurse.ui.schedule.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeBean;
import com.xiuxiu.confinement_nurse.ui.schedule.view.CalendarSwitchView;
import com.xiuxiu.confinement_nurse.ui.schedule.vm.ScheduleVm;
import com.xiuxiu.confinement_nurse.ui.view.OfficeItemView;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import org.jetbrains.annotations.NotNull;

public class ScheduleItemAdapter extends BaseQuickAdapter<ScheduleVm, BaseViewHolder> implements LoadMoreModule {
    XFunc1<Integer> xFunc0;

    public ScheduleItemAdapter(XFunc1<Integer> xFunc0) {
        super(R.layout.item_schedule);
        this.xFunc0 = xFunc0;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ScheduleVm scheduleVm) {
        CalendarSwitchView calendarSwitchView = (CalendarSwitchView) baseViewHolder.itemView;

        calendarSwitchView.setSelectCallBack(new XFunc0() {
            @Override
            public void call() {
                xFunc0.call(getItemPosition(scheduleVm));
            }
        });
        int itemPosition = getItemPosition(scheduleVm);
        if (itemPosition == 0) {
            calendarSwitchView.setTitle("本月档期");
        } else if (itemPosition == 1) {
            calendarSwitchView.setTitle("下月档期");
        } else {
            calendarSwitchView.setTitle("未来档期");
        }
        calendarSwitchView.loadData(scheduleVm);
    }
}
