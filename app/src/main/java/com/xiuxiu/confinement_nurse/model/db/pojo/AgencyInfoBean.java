package com.xiuxiu.confinement_nurse.model.db.pojo;

import androidx.room.ColumnInfo;

import java.io.Serializable;

public class AgencyInfoBean implements Serializable {
//     "id": 2,
//             "createTime": "2020-11-27 15:13:22",
//             "updateTime": "2020-11-27 16:54:07",
//             "title": "广东",
//             "orgId": 152,
//             "orgMobile": "0657-4432123",
//             "orgImg": null,
//             "address": null,
//             "contact": "许云舒",
//             "mobile": null,
//             "emails": null,
//             "des": null,
//             "tp": "0209",
//             "foreImg": "asd",
//             "tailImg": null,
//             "state": "0",
//             "ty": "04"
    @ColumnInfo(name = "agency_id")
    private String id;
    private String createTime;
    private String updateTime;
    private String title;
    private String orgId;
    private String orgMobile;
    private String orgImg;
    @ColumnInfo(name = "agency_address")
    private String address;
    private String contact;
    private String mobile;
    private String emails;
    private String des;
    private String tp;
    private String foreImg;
    private String tailImg;
    private String state;
    private String ty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgMobile() {
        return orgMobile;
    }

    public void setOrgMobile(String orgMobile) {
        this.orgMobile = orgMobile;
    }

    public String getOrgImg() {
        return orgImg;
    }

    public void setOrgImg(String orgImg) {
        this.orgImg = orgImg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getForeImg() {
        return foreImg;
    }

    public void setForeImg(String foreImg) {
        this.foreImg = foreImg;
    }

    public String getTailImg() {
        return tailImg;
    }

    public void setTailImg(String tailImg) {
        this.tailImg = tailImg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTy() {
        return ty;
    }

    public void setTy(String ty) {
        this.ty = ty;
    }

    @Override
    public String toString() {
        return "AgencyInfoBean{" +
                "id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", title='" + title + '\'' +
                ", orgId='" + orgId + '\'' +
                ", orgMobile='" + orgMobile + '\'' +
                ", orgImg='" + orgImg + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", mobile='" + mobile + '\'' +
                ", emails='" + emails + '\'' +
                ", des='" + des + '\'' +
                ", tp='" + tp + '\'' +
                ", foreImg='" + foreImg + '\'' +
                ", tailImg='" + tailImg + '\'' +
                ", state='" + state + '\'' +
                ", ty='" + ty + '\'' +
                '}';
    }
}
