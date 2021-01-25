package com.xiuxiu.confinement_nurse.ui.login;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;

public interface LoginContract {
    interface IView extends Viewer, LoadViewer {
        void onRequestLoginSuccess(String xtoken, String userId);

        void onRequestAginVerify();

        /**
         * 机构登录成功
         * @param xtoken
         * @param userId
         */
        void onRequestAgencyLoginSuccess(String xtoken, String userId);
    }

    interface IPresenter {
        public void requestLogin(String phone, String password,boolean isYuesao);

        public void requestLogout();
    }
}
