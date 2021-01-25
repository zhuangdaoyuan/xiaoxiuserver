package com.xiuxiu.confinement_nurse.ui.my;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;

import java.util.List;

public interface LearningExperienceContract {
    interface IView extends LoadViewer, PageStateViewer {

        void onRequestLearningData(List<FilterRadioVm> list);

        void onRequestDataEnd();
    }

    interface IPresenter {
        public void setId(String id);
        void requestLearningData();
        public void requestSubmit(String path, LearningExperienceVm.LearningExperience learningExperience);
        public void requestDelete(String id);

    }
}
