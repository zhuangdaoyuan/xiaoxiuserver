package com.xiuxiu.confinement_nurse.ui.schedule.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScheduleVm implements Serializable {

    /**
     * id : -2449617.108131185
     * startTime : exercitation se
     * endTime : nisi
     */

    private String id;
    private String startTime;
    private String endTime;

    private List<ScheduleVm> scheduleVms;

    private int month;
    private int year;
    private int day;


    public List<ScheduleVm> getScheduleVms() {
        return scheduleVms;
    }

    public void setScheduleVms(List<ScheduleVm> scheduleVms) {
        this.scheduleVms = scheduleVms;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
