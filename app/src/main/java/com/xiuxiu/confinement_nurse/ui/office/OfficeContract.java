package com.xiuxiu.confinement_nurse.ui.office;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderTitleVm;

import java.util.List;

public interface OfficeContract {
    interface IView extends LoadViewer , PageStateViewer {
        void onRequestTitleData(List<OrderTitleVm> orderTitleVm);
    }


    interface IPresenter {
        public void requestTitleData();
    }
}
