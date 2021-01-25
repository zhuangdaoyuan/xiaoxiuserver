package com.xiuxiu.confinement_nurse.ui.agency;


import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.agency.vm.MechanismManBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeBean;
import com.xiuxiu.confinement_nurse.ui.order.vm.PageBean;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

public class MechanismMainPresenter extends BasePresenter<MechanismMainContract.IView> implements MechanismMainContract.IPresenter {
    public MechanismMainPresenter(MechanismMainContract.IView viewer) {
        super(viewer);
    }

    private PageBean pageBean;
    private int pageNo = 1;

    @Override
    public void requestList() {
        pageNo = 1;
        loadData(pageNo, 30, new XFunc1<List<UserInfoBean>>() {
            @Override
            public void call(List<UserInfoBean> yuesaoInfoBeans) {
                getViewer().onRequestDataList(yuesaoInfoBeans);
                getViewer().onRequestPageSuccess();
            }
        }, new XFunc0() {
            @Override
            public void call() {
                getViewer().onRequestPageError(0);
            }
        });
    }

    @Override
    public void onLoadMore() {
        if (pageNo >= pageBean.getPageSize()) {
            getViewer().onRequestComplete();
            return;
        }
        int index = pageNo + 1;
        loadData(index, 30, officeBeans -> {
            pageNo++;
            getViewer().onRequestPageData(officeBeans);
        }, new XFunc0() {
            @Override
            public void call() {
                getViewer().onRequestPageDataError();
            }
        });
    }
    private void loadData(int pageNo, int pageSize, XFunc1<List<UserInfoBean>> xFunc1, XFunc0 errorCallback){
        RxHttp.postForm(Configuration.mechanism.KEY_YUESAO_LIST)
                .add("pageNo", pageNo)
                .add("pageSize", pageSize)
                .asXXResponse(MechanismManBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    pageBean = s.getPageBean();
                    if (s.getItems()==null||s.getItems().isEmpty()) {
                        s.setItems(new ArrayList<>());
                    }
                    xFunc1.call(s.getItems());
                }, throwable -> {
                    errorCallback.call();
                });
    }
}
