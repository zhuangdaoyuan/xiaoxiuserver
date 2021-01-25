package com.xiuxiu.confinement_nurse.ui.area.vm;

import com.contrarywind.interfaces.IPickerViewData;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CityVm implements Serializable {
    private List<City> items;


    public List<City> getItems() {
        return items;
    }

    public void setItems(List<City> items) {
        this.items = items;
    }
}
