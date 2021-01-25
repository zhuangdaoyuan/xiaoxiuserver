package com.xiuxiu.confinement_nurse.model.db.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.xiuxiu.confinement_nurse.ui.service.vm.MyServerInfoVm;

import java.io.Serializable;
import java.util.List;

public class UserInfoBean implements Serializable {

    /**
     * userName : 张三
     * avatarList : ["头像"]
     * sex : 1
     * age : 3
     * nativePlace : 1000
     * nativePlaceName : 湖北武汉
     * location : 1000
     * locationName : 武汉
     * address : 校区
     */

    private String userName;
    private int sex;
    private int age;
    private String nativePlace;
    private String nativePlaceName;
    private String location;
    private String locationName;
    private String address;
    private String userAvatar;
    private String idNo;
    private String workYears;
    private String avatarList;
    private String birthday;

    @ColumnInfo(name = "yuesao_id")
    private String id;

    @ColumnInfo(name = "yuesao_lng")
    private String lng;
    @ColumnInfo(name = "yuesao_lat")
    private String lat;
    @ColumnInfo(name = "yuesao_orgId")
    private String orgId;

    private String matronId;
    @Ignore
    private String state;
    @Ignore
    private String distributionCode;
    @Ignore
    private String highestDegree;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistributionCode() {
        return distributionCode;
    }

    public void setDistributionCode(String distributionCode) {
        this.distributionCode = distributionCode;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    @Ignore
    private List<MyServerInfoVm>  matronServiceList;

    public List<MyServerInfoVm> getMatronServiceList() {
        return matronServiceList;
    }

    public void setMatronServiceList(List<MyServerInfoVm> matronServiceList) {
        this.matronServiceList = matronServiceList;
    }

    @Ignore
    private List<String>  educationList;

    public List<String> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<String> educationList) {
        this.educationList = educationList;
    }

    public String getMatronId() {
        return matronId;
    }

    public void setMatronId(String matronId) {
        this.matronId = matronId;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getWorkYears() {
        return workYears;
    }

    public void setWorkYears(String workYears) {
        this.workYears = workYears;
    }

    public String getAvatarList() {
        return avatarList;
    }

    public void setAvatarList(String avatarList) {
        this.avatarList = avatarList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getNativePlaceName() {
        return nativePlaceName;
    }

    public void setNativePlaceName(String nativePlaceName) {
        this.nativePlaceName = nativePlaceName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
