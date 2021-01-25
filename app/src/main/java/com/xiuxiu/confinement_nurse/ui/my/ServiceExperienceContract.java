package com.xiuxiu.confinement_nurse.ui.my;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;

import java.util.List;

public interface ServiceExperienceContract {
    interface IView extends LoadViewer , PageStateViewer {

        void onRequestSubjectData(List<FilterRadioVm> list);

        void onRequestType(List<FilterRadioVm> list);

        void onRequestDataEnd();
    }

    interface IPresenter {
        public void setYsId(String ysId);
        void requestServiceData();
        public void requestDelete( String id);
        public void requestSubmit(String path, LearningExperienceVm.LearningExperience learningExperience);
    }
}
