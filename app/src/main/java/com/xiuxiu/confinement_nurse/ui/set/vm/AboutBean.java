package com.xiuxiu.confinement_nurse.ui.set.vm;

import java.io.Serializable;

public class AboutBean implements Serializable {
    private DataBean item;

    public DataBean getItem() {
        return item;
    }

    public void setItem(DataBean item) {
        this.item = item;
    }

    public static class DataBean {
        private String phone;
        private String email;
        private String qq;
        private String wx;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getWx() {
            return wx;
        }

        public void setWx(String wx) {
            this.wx = wx;
        }
    }
}
