package com.xiuxiu.confinement_nurse.ui.agency.user;


import android.net.Uri;

import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;

import java.util.List;

public interface AddYuesaoContract {
    interface IView extends LoadViewer {
        void onRequestOK();
    }

    interface IPresenter {
        public void requestUpdateUserInfo();

        public void requestUploadFile(List<String> paths, List<Uri> uris);


        void rquestName(String title);

        void requestSex(int i);

        void requestBirthday(String stampToDate);

        void setNativePlace(String nativePlace);

        void setNativePlaceName(String nativePlaceName);

        void setIdNo(String idCard);

        void setLocation(String code);

        void setAddress(String name);

        void setUserInfo(UserInfoBean userBean);
    }
}
