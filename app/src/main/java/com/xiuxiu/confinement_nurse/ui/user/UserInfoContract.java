package com.xiuxiu.confinement_nurse.ui.user;


import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

public interface UserInfoContract {
    interface IView extends Viewer {

    }

    interface IPresenter {

         void requestUserInfo(String xtoken, String userId, XFunc0 mSuccessCallBack, XFunc0 mErrorCallBack);
         void requestUserInfoByCertificate(XFunc1<Boolean> mSuccessCallBack, XFunc0 mErrorCallBack);
        void requestUserInfoByCertificate(String ysId,XFunc1<Boolean> mSuccessCallBack, XFunc0 mErrorCallBack);
         void requestAgencyInfo(String xtoken,String id,XFunc0 mSuucessCallBack,XFunc0 mErrorCallBack);
    }
}
