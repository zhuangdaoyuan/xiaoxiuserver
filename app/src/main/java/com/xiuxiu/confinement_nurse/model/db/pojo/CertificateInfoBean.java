package com.xiuxiu.confinement_nurse.model.db.pojo;

import java.util.List;

public class CertificateInfoBean {

    private List<CertificateInfo> items;

    public List<CertificateInfo> getItems() {
        return items;
    }

    public void setItems(List<CertificateInfo> items) {
        this.items = items;
    }

    public static class CertificateInfo{
        private String id;
        private String authType;
        private String img;
        private String state;
        private String verifyTime;
        private String reason;


        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getVerifyTime() {
            return verifyTime;
        }

        public void setVerifyTime(String verifyTime) {
            this.verifyTime = verifyTime;
        }
    }
}
