package com.xiuxiu.confinement_nurse.ui.order.vm;

import androidx.lifecycle.ViewModel;

public class OrderInfoViewModel extends ViewModel {
    private String matronId;
    private boolean isJiGou;

    public boolean isJiGou() {
        return isJiGou;
    }

    public void setJiGou(boolean jiGou) {
        isJiGou = jiGou;
    }

    public String getMatronId() {
        return matronId;
    }

    public void setMatronId(String matronId) {
        this.matronId = matronId;
    }
}
