package com.xiuxiu.confinement_nurse.ui.my;


import android.content.Context;

import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;

public interface SelfEvaluationContract {
    interface IView extends LoadViewer, PageStateViewer {
    }

    interface IPresenter {
        void requestSubmit(Context context,String path, String toString);

        public String getYsId() ;

        public void setYsId(String ysId);

    }
}
