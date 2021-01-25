package com.xiuxiu.confinement_nurse.ui.office;


import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilterPresenter extends BasePresenter<FilterContract.IView> implements FilterContract.IPresenter {

    public FilterPresenter(FilterContract.IView viewer) {
        super(viewer);
    }

    @Override
    public void requestFilterData(int deliveryType, int priceRangeType, int dayRangeType) {

        getViewer().onRequestResume(createResumeItem(deliveryType));
        getViewer().onRequestinterval(createIntervalItem(priceRangeType));
        getViewer().onRequestTime(createTimeItem(dayRangeType));
        getViewer().onRequestPageSuccess();

    }

    @Override
    public void requestSubmitData() {
    }

    private List<FilterRadioVm> createResumeItem(int deliveryType) {
        List<FilterRadioVm> list = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = UserHelper.orderByDeliveryType.entrySet();
        for (Map.Entry<String, String> map : entries) {
            FilterRadioVm filterRadioVm = new FilterRadioVm();
            filterRadioVm.setTitle(map.getValue());
            int code = Integer.parseInt(map.getKey());
            filterRadioVm.setCode(code);
            filterRadioVm.setState(deliveryType == code ? 1 : 0);
            list.add(filterRadioVm);
        }
        return list;
    }


    private List<FilterRadioVm> createIntervalItem(int priceRangeType) {
        List<FilterRadioVm> list = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = UserHelper.orderByPriceRangeType.entrySet();
        for (Map.Entry<String, String> map : entries) {
            FilterRadioVm filterRadioVm = new FilterRadioVm();
            filterRadioVm.setTitle(map.getValue());
            int code = Integer.parseInt(map.getKey());
            filterRadioVm.setCode(code);
            filterRadioVm.setState(priceRangeType == code ? 1 : 0);
            list.add(filterRadioVm);
        }
        return list;
    }


    private List<FilterRadioVm> createTimeItem(int dayRangeType) {
        List<FilterRadioVm> list = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = UserHelper.orderByTimeState.entrySet();
        for (Map.Entry<String, String> map : entries) {
            FilterRadioVm filterRadioVm = new FilterRadioVm();
            filterRadioVm.setTitle(map.getValue());
            int code = Integer.parseInt(map.getKey());
            filterRadioVm.setCode(code);
            filterRadioVm.setState(dayRangeType == code ? 1 : 0);
            list.add(filterRadioVm);
        }
        return list;
    }

}
