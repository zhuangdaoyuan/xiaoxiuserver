package com.xiuxiu.confinement_nurse.ui.my.vm;

import com.xiuxiu.confinement_nurse.ui.order.vm.OrderServiceVm;

import java.io.Serializable;
import java.util.List;

public class ServiceExperienceVm implements Serializable {
    private ServiceExperience item;


    public ServiceExperience getItem() {
        return item;
    }

    public void setItem(ServiceExperience item) {
        this.item = item;
    }

    public static class ServiceExperience implements Serializable {
        private String orderCount;
        private List<OrderServiceVm> orderItems;

        public String getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(String orderCount) {
            this.orderCount = orderCount;
        }

        public List<OrderServiceVm> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderServiceVm> orderItems) {
            this.orderItems = orderItems;
        }
    }
}
