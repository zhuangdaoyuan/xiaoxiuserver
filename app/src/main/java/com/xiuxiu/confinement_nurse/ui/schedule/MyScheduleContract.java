package com.xiuxiu.confinement_nurse.ui.schedule;


import com.haibin.calendarview.Calendar;
import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.schedule.vm.ScheduleVm;

import java.util.List;

public interface MyScheduleContract {
    interface IView extends PageStateViewer, LoadViewer {
        void onRequestListData(List<ScheduleVm> s);

        void onRequestSubmitSuccess();
    }

    interface IPresenter {
        void requestData();

        void requestSubmit(ScheduleVm safe1, List<Calendar> multiSelectCalendars);

        void setId(String id);
    }
}
