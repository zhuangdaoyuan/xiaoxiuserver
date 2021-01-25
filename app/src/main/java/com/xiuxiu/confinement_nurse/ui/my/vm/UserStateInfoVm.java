package com.xiuxiu.confinement_nurse.ui.my.vm;

import java.io.Serializable;

public class UserStateInfoVm implements Serializable {
    private UserStateInfo item;

    public UserStateInfo getItem() {
        return item;
    }

    public void setItem(UserStateInfo item) {
        this.item = item;
    }

    public static class UserStateInfo{

        /**
         * groupMatronNum : -7.824318339314696E7
         * orderNum : -6.94370078409901E7
         * totalBalance : 8.650543212258577E7
         * availableBalance : -2.77036977026885E7
         */

        private String groupMatronNum;
        private String orderNum;
        private String totalBalance;
        private String availableBalance;

        public String getGroupMatronNum() {
            return groupMatronNum;
        }

        public void setGroupMatronNum(String groupMatronNum) {
            this.groupMatronNum = groupMatronNum;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getTotalBalance() {
            return totalBalance;
        }

        public void setTotalBalance(String totalBalance) {
            this.totalBalance = totalBalance;
        }

        public String getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(String availableBalance) {
            this.availableBalance = availableBalance;
        }
    }
}
