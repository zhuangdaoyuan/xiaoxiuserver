package com.xiuxiu.confinement_nurse.ui.my;


import android.net.Uri;

import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;

import java.util.List;

public interface MyInfoContract {
    interface IView extends LoadViewer {
        void onRequestUploadFileSuccess(String mImageUrls, List<Uri> uris);
    }

    interface IPresenter {

        public String getYsid();

        public void setYsid(String ysid);
        public void requestUpdateUserInfo(UserInfoBean userBean, XFunc0 xFunc0);

        public void requestUploadFile(List<String> paths, List<Uri> uris);
    }
}
