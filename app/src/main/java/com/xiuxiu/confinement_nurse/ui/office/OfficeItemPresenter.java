package com.xiuxiu.confinement_nurse.ui.office;


import android.text.TextUtils;

import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeBean;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeItemVm;
import com.xiuxiu.confinement_nurse.ui.order.vm.PageBean;
import com.xiuxiu.confinement_nurse.utlis.NetUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.param.RxHttp;

import static com.xiuxiu.confinement_nurse.help.UserHelper.KEY_DEFAULT_CITY_CODE;

public class OfficeItemPresenter extends BasePresenter<OfficeItemContract.IView> implements OfficeItemContract.IPresenter {

    private PageBean pageBean;
    private String type;
    private String locationCityCode;
    private int pageNo = 1;

    public OfficeItemPresenter(OfficeItemContract.IView viewer) {
        super(viewer);
    }


    @Override
    public void requestOfficeData(String type) {

        City locationCity = ModelManager.getInstance().getCacheInterface().getLocationCity();
        if (locationCity != null) {
            locationCityCode = locationCity.getCode();
        }
        if (TextUtils.isEmpty(locationCityCode)) {
            locationCityCode = KEY_DEFAULT_CITY_CODE;
        }
        this.type = type;
        getViewer().onRequestRefreshing(true);
        pageNo = 1;
        loadData(pageNo, 30, new XFunc1<List<OfficeBean>>() {
            @Override
            public void call(List<OfficeBean> officeBeans) {
                if (officeBeans.isEmpty()) {
                    getViewer().onRequestPageEmpty();
                } else {
                    getViewer().onRequestOfficeData(officeBeans);
                    getViewer().onRequestPageSuccess();
                }
                getViewer().onRequestRefreshing(false);
            }
        }, new XFunc0() {
            @Override
            public void call() {
                getViewer().onRequestRefreshing(false);
                if (NetUtil.isNetworkAvailable()) {
                    getViewer().onRequestPageError(0);
                } else {
                    getViewer().onRequestPageNetError();
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        if (pageNo >= pageBean.getPageSize()) {
            getViewer().onRequestOfficeComplete();
            return;
        }
        int index = pageNo + 1;
        loadData(index, 30, new XFunc1<List<OfficeBean>>() {
            @Override
            public void call(List<OfficeBean> officeBeans) {
                pageNo++;
                getViewer().onRequestOfficePageData(officeBeans);
            }
        }, new XFunc0() {
            @Override
            public void call() {
                getViewer().onRequestOfficePageDataError();
            }
        });
    }

    private void loadData(int pageNo, int pageSize, XFunc1<List<OfficeBean>> xFunc1, XFunc0 errorCallback) {
        int dayRangeType = ModelManager.getInstance().getCacheInterface().getDayRangeType();
        int priceRangeType = ModelManager.getInstance().getCacheInterface().getPriceRangeType();
        int deliveryType = ModelManager.getInstance().getCacheInterface().getDeliveryType();

        Observable<OfficeItemVm> officeItemVmObservable = null;
        if (TextUtils.equals(type, RouterHelper.Constant.KEY_RECOMMEND)) {
            officeItemVmObservable = RxHttp.get(Configuration.Office.KEY_RECOMMEND)
                    .add("city", locationCityCode)
                    .add("deliveryType", dayRangeType)
                    .add("priceRangeType", priceRangeType)
                    .add("dayRangeType", deliveryType)
                    .add("pageNo", pageNo)
                    .add("pageSize", pageSize)
                    .asXXResponse(OfficeItemVm.class);
        } else if (TextUtils.equals(type, RouterHelper.Constant.KEY_ROUND)) {
            City locationCity1 = ModelManager.getInstance().getCacheInterface().getLocationCity();
            officeItemVmObservable = RxHttp.get(Configuration.Office.KEY_NEARBY)
                    .add("city", locationCityCode)
                    .add("deliveryType", dayRangeType)
                    .add("priceRangeType", priceRangeType)
                    .add("dayRangeType", deliveryType)
                    .add("lat", locationCity1.getLatitude())
                    .add("lng", locationCity1.getLongitude())
                    .asXXResponse(OfficeItemVm.class);
        } else if (TextUtils.equals(type, RouterHelper.Constant.KEY_LATEST)) {
            officeItemVmObservable = RxHttp.get(Configuration.Office.KEY_LATEST)
                    .add("city", locationCityCode)
                    .add("deliveryType", dayRangeType)
                    .add("priceRangeType", priceRangeType)
                    .add("dayRangeType", deliveryType)
                    .add("pageNo", pageNo)
                    .add("pageSize", pageSize)
                    .asXXResponse(OfficeItemVm.class);
        }
        if (officeItemVmObservable == null) {
            getViewer().onRequestPageEmpty();
            return;
        }
        officeItemVmObservable
                .doOnNext(new Consumer<OfficeItemVm>() {
                    @Override
                    public void accept(OfficeItemVm officeItemVm) throws Exception {
                        pageBean = officeItemVm.getPageBean();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(new Consumer<OfficeItemVm>() {
                    @Override
                    public void accept(OfficeItemVm officeItemVm) throws Exception {
                        List<OfficeBean> items = officeItemVm.getItems();
                        if (xFunc1 != null) {
                            xFunc1.call(items == null ? new ArrayList<>() : items);
                        }
                    }
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    }
                    if (errorCallback != null) {
                        errorCallback.call();
                    }

                });
    }

    ///////////////投递简历////////////////////////////////////
    @Override
    public void requestDelivery(int position, String employmentId) {
        RxHttp.postForm(Configuration.Office.KEY_POST_RESUME)
                .add("employmentId", employmentId)
                .asXXResponse(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewer().showLoadingDialog())
                .doFinally(() -> getViewer().cancelLoadingDialog())
                .doOnError(throwable -> getViewer().cancelLoadingDialog())
                .as(RxLife.as(getViewer()))
                .subscribe(s -> {
                    getViewer().onRequestDelivery(position);
                }, throwable -> {
                    if (throwable instanceof ParseException) {
                        ToastHelper.showToast(throwable.getMessage());
                    } else {
                        ToastHelper.showToast("投递失败");
                    }
                });
    }
}

