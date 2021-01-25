package com.xiuxiu.confinement_nurse.ui.my;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.my.vm.OtherExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.PathVm;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;

import java.util.List;

public interface OtherExperienceContract {
    interface IView extends LoadViewer, PageStateViewer {

        void onRequestService(List<FilterRadioVm> list);

        void onRequestType(List<FilterRadioVm> list);

        void onRequestData();

        void onRequestSubmitSuccess();
    }

    interface IPresenter {
        public String getYsId() ;
        public void setYsId(String ysId);

        void requestData();
        public void requestSubmit(List<PathVm> path, OtherExperienceVm.OtherExperience learningExperience);
        public void requestSubmit(List<PathVm> path, String id, OtherExperienceVm.OtherExperience learningExperience);

        void requestDelete(String id);
    }
}
