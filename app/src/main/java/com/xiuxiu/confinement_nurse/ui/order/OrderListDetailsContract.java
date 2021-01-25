package com.xiuxiu.confinement_nurse.ui.order;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderBean;

public interface OrderListDetailsContract {
    interface IView extends PageStateViewer, LoadViewer {
        void onRequestDetails(OrderBean s);
    }

    interface IPresenter{
        public void requestLoadData(String orderid);
        public void setMatronId(String matronId);
    }
}
