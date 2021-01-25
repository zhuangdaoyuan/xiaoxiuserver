package com.xiuxiu.confinement_nurse.ui.office;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeBean;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeItemVm;

import java.util.List;

public interface OfficeItemContract {
    interface IView extends PageStateViewer , LoadViewer {

        void onRequestOfficeData(List<OfficeBean> orderTitleVms);

        void onRequestRefreshing(boolean b);

        void onRequestOfficePageData(List<OfficeBean> officeBeans);

        void onRequestOfficePageDataError();

        void onRequestDelivery(int position);

        void onRequestOfficeComplete();
    }

    interface IPresenter {
        void requestOfficeData(String type);

        void onLoadMore();


        public void requestDelivery(int position, String employmentId);
    }
}
