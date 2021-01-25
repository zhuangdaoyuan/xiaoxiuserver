package com.xiuxiu.confinement_nurse.ui.office.vm;

import java.io.Serializable;

public class OfficeBean implements Serializable {

    /**
     * employmentId : null
     * userId : -6.544075476347024E7
     * nickName : aute non ex
     * userAvatar : ut consequat repreh
     * startTime : veniam in id ad nu
     * endTime : in
     * serviceType : -4.925459896755497E7
     * priceRangeType : 5.756299812545994E7
     * minPrice : -3.647268382755589E7
     * maxPrice : -1.0803723345955163E7
     * workYears : 9.12318156960665E7
     * specialRequire : cupidatat
     * otherSkill : eiusmod occaecat d
     * location : -9.91348513444135E7
     * address : sed mollit exercitation minim consectetur
     * lng : -7.753245502273826E7
     * lat : 9.863435330124414E7
     * deliveryState : null
     */

    private String employmentId;
    private String userId;
    private String nickName;
    private String userAvatar;
    private String startTime;
    private String endTime;

    /**
     * 服务类型
     */
    private String serviceType;
    /**
     * 特殊要求
     */
    private String specialRequire;
    /**
     * 其他要求
     */
    private String otherSkill;
    /**
     * 投递状态
     */
    private String deliveryState;

    /**
     * 价格类型
     */
    private String priceRangeType;
    private String minPrice;
    private String maxPrice;
    private String workYears;


    private String location;
    private String address;
    private String lng;
    private String lat;
    private String minAge;
    private String maxAge;
    private String education;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getEmploymentId() {
        return employmentId;
    }

    public void setEmploymentId(String employmentId) {
        this.employmentId = employmentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPriceRangeType() {
        return priceRangeType;
    }

    public void setPriceRangeType(String priceRangeType) {
        this.priceRangeType = priceRangeType;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getWorkYears() {
        return workYears;
    }

    public void setWorkYears(String workYears) {
        this.workYears = workYears;
    }

    public String getSpecialRequire() {
        return specialRequire;
    }

    public void setSpecialRequire(String specialRequire) {
        this.specialRequire = specialRequire;
    }

    public String getOtherSkill() {
        return otherSkill;
    }

    public void setOtherSkill(String otherSkill) {
        this.otherSkill = otherSkill;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }
}
