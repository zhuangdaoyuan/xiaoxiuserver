package com.lxj.xpopupext.listener;

import android.view.View;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created by xiaosong on 2018/3/20.
 */

public interface CityPickerListener {

    void onCityConfirm(IPickerViewData province, IPickerViewData city, IPickerViewData area, View v);
    void onCityChange(IPickerViewData province, IPickerViewData city, IPickerViewData area);

}
