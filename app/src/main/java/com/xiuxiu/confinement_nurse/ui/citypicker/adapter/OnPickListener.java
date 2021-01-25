package com.xiuxiu.confinement_nurse.ui.citypicker.adapter;


import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;

public interface OnPickListener {
    void onPick(int position, City data);
    void onLocate();
    void onCancel();
}
