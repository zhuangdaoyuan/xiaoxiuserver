package com.xiuxiu.confinement_nurse.ui.citypicker.adapter;


import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;

public interface InnerListener {
    void dismiss(int position, City data);
    void locate();
}
