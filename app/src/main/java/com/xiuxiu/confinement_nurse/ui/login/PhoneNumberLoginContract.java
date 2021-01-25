package com.xiuxiu.confinement_nurse.ui.login;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;

public interface PhoneNumberLoginContract {
    interface IView extends LoadViewer{

        void onRequestPostCodeNum(long l);

        void onRequestPostCodeNumComplete();

        void onRequestRegisterSuccess();

        void onRequestNext(String phone,String code);

        void onRequestResetPasswordSuccess();

        void onRequestChangeSuccess();
    }

    interface IPresenter {
        public void requestResetPasswordByPassword(String oldPassword,String newPassword);
        public void requestResetPasswordCode(String phone);
        public void requestResetPasswordCheck(String phone, String code);
        public void requestPostCode(String phone);
        public void requestPostCode(String phone,int userType);
        public void requestRegister(String phone, String password, String code);

        public void requestChangePhone(String phone, String code);

        void requestResetPasswordByPassword(String phone, String code, String newPassword, String password);
    }
}
