package com.xiuxiu.confinement_nurse.ui.office.vm;

import java.io.Serializable;

public class FilterRadioVm implements Serializable {
    private String title;
    private int state;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
