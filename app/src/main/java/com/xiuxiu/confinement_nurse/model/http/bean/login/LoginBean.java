package com.xiuxiu.confinement_nurse.model.http.bean.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginBean implements Serializable {
    private String xtoken;
    @SerializedName("user_id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getXtoken() {
        return xtoken;
    }

    public void setXtoken(String xtoken) {
        this.xtoken = xtoken;
    }
}
