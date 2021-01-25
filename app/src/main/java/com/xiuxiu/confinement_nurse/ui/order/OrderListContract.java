package com.xiuxiu.confinement_nurse.ui.order;


import com.xiuxiu.confinement_nurse.ui.base.mvp.LoadViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.PageStateViewer;
import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderBean;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderListItemVm;

import java.util.List;

public interface OrderListContract {
    interface IView extends LoadViewer, PageStateViewer {
        void onRequestDataList(List<OrderBean> items);

        //拒绝成功
        void onRequestRefuseSuccess();

        void onRequestConfirmSuccess();

        void onRequestOrderComplete();

        void onRequestOrderError();

        void onRequestRefreshing(boolean b);

        void onRequestNoPage();
    }

    interface IPresenter {
        public void setJiGoud(boolean jiGoud);
        void requestOrderData(String type);

         void requestLoadMore();

        /**
         * 拒绝订单
         * @param o
         */
        void requestRefuse(OrderBean o);

        /**
         * 操作订单
         * @param o
         */
        void requestOperateOrder(OrderBean o);

        void requestYsId(String matronId);
    }
}
