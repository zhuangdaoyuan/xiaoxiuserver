package com.xiuxiu.confinement_nurse.ui.office;


import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderTitleVm;

import java.util.ArrayList;
import java.util.List;

public class OfficePresenter extends BasePresenter<OfficeContract.IView> implements OfficeContract.IPresenter {

    public OfficePresenter(OfficeContract.IView viewer) {
        super(viewer);
    }

    @Override
    public void requestTitleData() {

        List<OrderTitleVm> orderTitleVms = new ArrayList<>();

        OrderTitleVm orderTitleVm = new OrderTitleVm();
        orderTitleVm.setTitle("推荐");
        orderTitleVm.setType(RouterHelper.Constant.KEY_RECOMMEND);
        orderTitleVms.add(orderTitleVm);

        orderTitleVm = new OrderTitleVm();
        orderTitleVm.setTitle("附近");
        orderTitleVm.setType(RouterHelper.Constant.KEY_ROUND);
        orderTitleVms.add(orderTitleVm);

        orderTitleVm = new OrderTitleVm();
        orderTitleVm.setTitle("最新");
        orderTitleVm.setType(RouterHelper.Constant.KEY_LATEST);
        orderTitleVms.add(orderTitleVm);

        getViewer().onRequestPageSuccess();
        getViewer().onRequestTitleData(orderTitleVms);
    }
}

