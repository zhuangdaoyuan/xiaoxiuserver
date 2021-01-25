package com.xiuxiu.confinement_nurse.ui.login;

import android.net.Uri;

import com.xiuxiu.confinement_nurse.model.db.pojo.AgencyInfoBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;

import java.util.List;

public interface AgencyRegisterContract {
    interface IView extends LoadViewer {
        void onRequestRegisterSuccess();

        void onRequestAgencyInfo(AgencyInfoBean loginBean);
    }

    interface IPresenter {
        void requestUploadFile(List<String> mPaths, List<Uri> uris);
        public void requestAgencyInfo(String phone);
        void requestSub(String phone,String code,String password,String name,String person, String title1,String s);

        /**
         *
         * @param phone 注册号码
         * @param title 机构名称
         * @param contact 机构联系人
         * @param orgMobile 机构客服电话
         */
        void requestAgainSub(String phone,String title,String contact, String orgMobile);

    }
}
