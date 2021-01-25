package com.xiuxiu.confinement_nurse.ui.my;

import android.net.Uri;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xiuxiu.confinement_nurse.model.db.pojo.CertificateInfoBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.base.view.consecutivescroller.ConsecutiveScrollerLayout;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.OtherExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.SelfExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.ServiceExperienceVm;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

public interface MyContract {
    interface IView extends LoadViewer, PageStateViewer {
        void onRequsetCertification(int requestCode, Uri path);

        void onRequestLearningExperience(LearningExperienceVm learningExperienceVm);

        void onRequestOtherExperienceVm(OtherExperienceVm otherExperienceVm);

        void onRequestServiceExperience(ServiceExperienceVm serviceExperienceVm);

        void onRequestSelfExperience(SelfExperienceVm selfExperienceVm);

        void loadUserCertificateInfoState(CertificateInfoBean certificateInfoBean);
    }

    interface IPresenter {
        public void setYsId(String ysId);
        String getYsId();
        public void requestUploadCertification(int requestCode, Uri uri, String path, String idCard);

        public void requestLoadData();

        public void requestLoadOtherList();

        public void requestLoadSelfExperience();

        public void requestLoadEducationList();


        public void createUploadUrl(ViewGroup view, ConsecutiveScrollerLayout activityMyBg, XFunc1<String> callback, XFunc0 errorCallBack);
    }
}
