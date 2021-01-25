package com.xiuxiu.confinement_nurse.ui.news.vm;

import java.io.Serializable;
import java.util.List;


public class UserInfoVm implements Serializable {
    private List<UserInfo> items;

    public List<UserInfo> getItems() {
        return items;
    }

    public void setItems(List<UserInfo> items) {
        this.items = items;
    }

    public static class UserInfo{
        private String userId;
        private String nickName;
        private String portrait;

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

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }
    }
}
