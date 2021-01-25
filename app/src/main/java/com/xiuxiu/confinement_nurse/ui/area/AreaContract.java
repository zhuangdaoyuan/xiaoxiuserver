package com.xiuxiu.confinement_nurse.ui.area;


import com.xiuxiu.confinement_nurse.ui.area.vm.CityVm;
import com.xiuxiu.confinement_nurse.ui.area.vm.LocationVm;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;

public interface AreaContract {
    interface IView extends Viewer {
        void onRequestCitys(CityVm locationVm);

        void onRequestLocations(LocationVm locationVm);
    }

    interface IPresenter  {
        public void requestLocationData();
        public void requestLocations();
        public void requestCitys();
    }
}
