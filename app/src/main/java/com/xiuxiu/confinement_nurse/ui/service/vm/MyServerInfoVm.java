package com.xiuxiu.confinement_nurse.ui.service.vm;

import java.io.Serializable;

public  class MyServerInfoVm  implements Serializable {

    /**
     * id : 1
     * serviceType : 1
     * specialCare : 1
     * otherSkills : 1
     * days : 26
     * totalPrice : 1000
     * timeType : 1
     */

    private String id;
    private String serviceType;
    private String specialCare;
    private String otherSkills;
    private String days;
    private String totalPrice;
    private String timeType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSpecialCare() {
        return specialCare;
    }

    public void setSpecialCare(String specialCare) {
        this.specialCare = specialCare;
    }

    public String getOtherSkills() {
        return otherSkills;
    }

    public void setOtherSkills(String otherSkills) {
        this.otherSkills = otherSkills;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }
}
