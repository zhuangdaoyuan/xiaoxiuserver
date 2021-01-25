package com.xiuxiu.confinement_nurse.ui.area.vm;


import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocationVm implements Serializable {
    private List<City> items;


    public List<City> getItems() {
        return items;
    }

    public void setItems(List<City> items) {
        this.items = items;
    }

}
