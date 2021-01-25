package com.xiuxiu.confinement_nurse.ui.service;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.service.vm.MyServerInfoVm;

import java.util.List;

public interface MyServiceContract {
    interface IView extends LoadViewer, PageStateViewer {
        void onRequestMyServiceList(List<MyServerInfoVm> s);

        void onRequestDeleteSuccess(int position);
    }

    interface IPresenter  {
        public void setYsId(String ysId);
        void requestMyServiceListData();

        void requestSubmitService(MyServerInfoVm myServerInfoVm);

        void requestDelete(int position,MyServerInfoVm safe);

        void requestEditData(MyServerInfoVm myServerInfoVm);
    }
}
