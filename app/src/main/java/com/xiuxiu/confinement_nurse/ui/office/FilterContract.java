package com.xiuxiu.confinement_nurse.ui.office;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;

import java.util.List;

public interface FilterContract {
    interface IView extends Viewer, PageStateViewer, LoadViewer {

        void onRequestResume(List<FilterRadioVm> resumeItem);

        void onRequestinterval(List<FilterRadioVm> intervalItem);

        void onRequestTime(List<FilterRadioVm> timeItem);
    }

    interface IPresenter  {
        public void requestFilterData(int deliveryType, int priceRangeType, int dayRangeType);

        void requestSubmitData();
    }
}
