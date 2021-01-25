package com.xiuxiu.confinement_nurse.ui.order.vm;

import java.io.Serializable;

public  class ContractVm implements Serializable {
    private ContractDetailVm item;

    public ContractDetailVm getItem() {
        return item;
    }

    public void setItem(ContractDetailVm item) {
        this.item = item;
    }

    public static class ContractDetailVm implements Serializable {

        /**
         * userName : dolor id culpa
         * userIdNo : in aliquip ullamco anim
         * matronName : cupidatat dolor voluptate adipisicing Lorem
         * matronIdNo : ipsum
         */

        private String userName;
        private String userIdNo;
        private String matronName;
        private String matronIdNo;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserIdNo() {
            return userIdNo;
        }

        public void setUserIdNo(String userIdNo) {
            this.userIdNo = userIdNo;
        }

        public String getMatronName() {
            return matronName;
        }

        public void setMatronName(String matronName) {
            this.matronName = matronName;
        }

        public String getMatronIdNo() {
            return matronIdNo;
        }

        public void setMatronIdNo(String matronIdNo) {
            this.matronIdNo = matronIdNo;
        }
    }
}
