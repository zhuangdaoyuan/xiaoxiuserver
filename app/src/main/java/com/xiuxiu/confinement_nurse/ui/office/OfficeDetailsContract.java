package com.xiuxiu.confinement_nurse.ui.office;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeBean;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeDetailsVm;

public interface OfficeDetailsContract {
    interface IView extends LoadViewer, PageStateViewer {
        void onRequestOrderDetails(OfficeBean orderDetailsVm);

        void onRequestDelivery();
    }

    interface IPresenter {
        void requestOrderDetails(String stringExtra);

        public void requestDelivery(String employmentId);
    }
}
