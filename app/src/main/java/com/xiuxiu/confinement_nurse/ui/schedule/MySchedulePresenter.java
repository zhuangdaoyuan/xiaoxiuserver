package com.xiuxiu.confinement_nurse.ui.schedule;


import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.schedule.vm.ScheduleListVm;
import com.xiuxiu.confinement_nurse.ui.schedule.vm.ScheduleVm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttpFormParam;
import rxhttp.wrapper.param.RxHttpNoBodyParam;

public class MySchedulePresenter extends BasePresenter<MyScheduleContract.IView> implements MyScheduleContract.IPresenter {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private String name;
    private String id;
    @Override
    public void setId(String id) {
        this.id = id;
    }

    public MySchedulePresenter(MyScheduleContract.IView viewer) {
        super(viewer);
    }

    /**
     * 查找一年内的档期
     */
    @Override
    public void requestData() {
        getViewer().onRequestLoading();
        long l = System.currentTimeMillis();
        Date currentDate = new Date();
        currentDate.setTime(l);
        String startTime = DateHelper.stampToDate(String.valueOf(getTodayZero(currentDate)));


        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
        Date date = curr.getTime();

        String endTime = DateHelper.stampToDate(date);

        Observable<ScheduleListVm> scheduleListVmObservable;
        if (TextUtils.isEmpty(id)) {
            RxHttpNoBodyParam rxHttpNoBodyParam ;
            rxHttpNoBodyParam= RxHttp.get(Configuration.Schedule.KEY_SCHEDULE_LIST);
            scheduleListVmObservable=   rxHttpNoBodyParam
                    .add("startTime", startTime)
                    .add("endTime", endTime)
                    .asXXResponse(ScheduleListVm.class);
        }else{
            RxHttpFormParam ysId = RxHttp.postForm(Configuration.mechanism.KEY_SCHEDULE_LIST)
                    .add("ysId", id);
          scheduleListVmObservable = ysId.add("startTime", startTime)
                    .add("endTime", endTime)
                    .asXXResponse(ScheduleListVm.class);
        }
        scheduleListVmObservable    .map(new Function<ScheduleListVm, List<ScheduleVm>>() {
                    @Override
                    public List<ScheduleVm> apply(ScheduleListVm scheduleListVm) throws Exception {
                        List<ScheduleVm> items = scheduleListVm.getItems();
                        return items == null ? new ArrayList<>() : items;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    Map<String, ScheduleVm> scheduleVmMap = new LinkedHashMap<>();
                    //创建未来11个月的档期
                    Calendar curr2;
                    for (int i = 0; i < 11; i++) {
                        ScheduleVm scheduleVm = new ScheduleVm();
                        curr2 = Calendar.getInstance();
                        curr2.set(Calendar.MONTH, curr.get(Calendar.MONTH) + i);
                        int i1 = curr2.get(Calendar.MONTH) + 1;
                        int year = curr2.get(Calendar.YEAR);
                        scheduleVm.setMonth(i1);
                        scheduleVm.setYear(year);

                        scheduleVmMap.put(year + "-" + i1, scheduleVm);
                    }


                    int size = s.size();
                    if (size != 0) {
                        Date startDate;
                        for (int i = 0; i < size; i++) {
                            ScheduleVm scheduleVm = s.get(i);
                            String startTime1 = scheduleVm.getStartTime();
                            if (!TextUtils.isEmpty(startTime1)) {
                                try {
                                    startDate = simpleDateFormat.parse(startTime1);
                                    curr.setTime(startDate);

                                    int startMonth = curr.get(Calendar.MONTH) + 1;
                                    int startYear = curr.get(Calendar.YEAR);
                                    int day = curr.get(Calendar.DAY_OF_MONTH);

                                    scheduleVm.setMonth(startMonth);
                                    scheduleVm.setYear(startYear);
                                    scheduleVm.setDay(day);


                                    ScheduleVm scheduleVm1 = scheduleVmMap.get(startYear + "-" + startMonth);
                                    if (scheduleVm1 != null) {
                                        if (scheduleVm1.getScheduleVms() == null) {
                                            scheduleVm1.setScheduleVms(new ArrayList<>());
                                        }
                                        scheduleVm1.getScheduleVms().add(scheduleVm);
                                        scheduleVmMap.put(startYear + "-" + startMonth, scheduleVm1);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    getViewer().onRequestListData(new ArrayList<>(scheduleVmMap.values()));
                    getViewer().onRequestPageSuccess();
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                    getViewer().onRequestPageError(0);
                });
    }

    @Override
    public void requestSubmit(ScheduleVm safe1, List<com.haibin.calendarview.Calendar> multiSelectCalendars) {

//        Map<String, ScheduleVm> originalData = new LinkedHashMap<>();
        Map<String, com.haibin.calendarview.Calendar> addData = new LinkedHashMap<>();
        List<Observable<String>> observableData = new ArrayList<>();

        for (int i = 0; i < multiSelectCalendars.size(); i++) {
            com.haibin.calendarview.Calendar scheduleVm = multiSelectCalendars.get(i);
            addData.put(scheduleVm.getYear() + "-" + scheduleVm.getMonth() + "-" + scheduleVm.getDay(), scheduleVm);
        }


        List<ScheduleVm> originalScheduleVms = safe1.getScheduleVms();
        if (originalScheduleVms != null && originalScheduleVms.size() > 0) {
            for (int i = 0; i < originalScheduleVms.size(); i++) {
                ScheduleVm scheduleVm = originalScheduleVms.get(i);
                if (!TextUtils.isEmpty(scheduleVm.getId())) {
                    String key = scheduleVm.getYear() + "-" + scheduleVm.getMonth() + "-" + scheduleVm.getDay();
//                    originalData.put(key, scheduleVm);

                    if (addData.containsKey(key)) {
                        addData.remove(key);
                    } else {
                        observableData.add(createDeleteSubmit(scheduleVm.getId()));
                    }
                }
            }
        }
        Set<Map.Entry<String, com.haibin.calendarview.Calendar>> entries = addData.entrySet();
        for (Map.Entry<String, com.haibin.calendarview.Calendar> map:entries){
            com.haibin.calendarview.Calendar value = map.getValue();
            String[] s = s(value.getTimeInMillis());
            observableData.add(createSubmitData(s[0], s[1]));
        }
        Observable.merge(observableData)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastHelper.showToast("提交成功");
                        getViewer().onRequestSubmitSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof ParseException) {
                            ToastHelper.showToast(throwable.getMessage());
                        }
                    }
                });
    }

    private Observable<String> createDeleteSubmit(String id) {
        RxHttpFormParam rxHttpFormParam;
        if (TextUtils.isEmpty(this.id)) {
             rxHttpFormParam = RxHttp.postForm(Configuration.Schedule.KEY_SCHEDULE_DELETE);
        }else{
            rxHttpFormParam = RxHttp.postForm(Configuration.mechanism.KEY_SCHEDULE_DELETE)
                    .add("ysId",this.id);
        }
        return rxHttpFormParam
                .add("id", id)
                .asXXResponse(String.class);
    }

    private Observable<String> createSubmitData(String startTime, String endTime) {
        RxHttpFormParam rxHttpFormParam;
        if (TextUtils.isEmpty(id)) {
             rxHttpFormParam = RxHttp.postForm(Configuration.Schedule.KEY_SCHEDULE_ADD);
        }else{
            rxHttpFormParam = RxHttp.postForm(Configuration.mechanism.KEY_SCHEDULE_ADD)
                    .add("ysId",id);
        }
        return rxHttpFormParam
                .add("startTime", startTime)
                .add("endTime", endTime)
                .asXXResponse(String.class);
    }


    public String[] s(long start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start);

        //一天的开始时间 yyyy:MM:dd 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();

        String startStr = simpleDateFormat.format(dayStart);

        //一天的结束时间 yyyy:MM:dd 23:59:59
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date dayEnd = calendar.getTime();
        String endStr = simpleDateFormat.format(dayEnd);

        return new String[]{startStr, endStr};

    }


    public static long getTodayZero(Date date) {
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }
}
