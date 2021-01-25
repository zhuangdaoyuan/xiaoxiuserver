package com.xiuxiu.confinement_nurse.ui.order.vm;

import java.io.Serializable;
import java.util.List;

public class OrderServiceVm implements Serializable {

    /**
     * orderId : -3.920876375065738E7
     * serviceType : 5.412884975055316E7
     * serviceStartTime : sunt in dolore in
     * serviceEndTime : do Excepteur et sit commodo
     * serviceDays : 7.807289401454765E7
     * location : -7.29049785929448E7
     * locationName : nisi
     * userComment : sed
     */

    private String orderId;
    private String serviceType;
    private String serviceStartTime;
    private String serviceEndTime;
    private String serviceDays;
    private String location;
    private String locationName;
    private String userComment;

    private String userCommentImgs;

    public String getUserCommentImgs() {
        return userCommentImgs;
    }

    public void setUserCommentImgs(String userCommentImgs) {
        this.userCommentImgs = userCommentImgs;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public String getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(String serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public String getServiceDays() {
        return serviceDays;
    }

    public void setServiceDays(String serviceDays) {
        this.serviceDays = serviceDays;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }
}
