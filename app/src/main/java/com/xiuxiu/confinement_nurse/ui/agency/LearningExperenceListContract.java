package com.xiuxiu.confinement_nurse.ui.agency;

import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;

class LearningExperenceListContract {
    interface IView extends LoadViewer, PageStateViewer {

        void onRequestLearningExperience(LearningExperienceVm otherExperienceVm);
    }

    interface IPresenter {

        void requestLoad(String matronId);
    }
}
