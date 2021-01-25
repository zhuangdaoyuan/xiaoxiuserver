package com.xiuxiu.confinement_nurse.ui.news;


import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.ui.news.vm.TokenVm;
import com.xiuxiu.confinement_nurse.ui.news.vm.UserInfoVm;

public interface NewsContract {
    interface IView extends Viewer {
        void onRequestUserToken(TokenVm s);

        void onRequestCustomerServiceToken(TokenVm s);

        void onRequestUserInfo(UserInfoVm s);
    }

    interface IPresenter {
        /**
         * 获取用户对话token
         * @param id
         */
        public void requestUserToken(String id);

        /**
         * 获取客服token
         */
        public void requestCustomerServiceToken();
    }
}
