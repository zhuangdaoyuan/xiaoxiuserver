package com.xiuxiu.confinement_nurse.ui.agency;


import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.ui.agency.vm.MechanismManBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;

import java.util.List;

public interface MechanismMainContract {
    interface IView extends LoadViewer, PageStateViewer {

        void onRequestDataList(List<UserInfoBean> s);

        void onRequestComplete();

        void onRequestPageData(List<UserInfoBean> officeBeans);

        void onRequestPageDataError();
    }

    interface IPresenter {
        void requestList();

        void onLoadMore();
    }
}
