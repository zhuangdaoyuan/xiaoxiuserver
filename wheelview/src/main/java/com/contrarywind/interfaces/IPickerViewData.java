package com.contrarywind.interfaces;

import java.util.List;

/**
 * Created by Sai on 2016/7/13.
 */
public interface IPickerViewData {
    String getPickerViewText();

    String getPickerViewCode();

    List<IPickerViewData> getPickerChilch();
}
