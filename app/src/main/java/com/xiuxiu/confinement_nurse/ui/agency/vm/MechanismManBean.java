package com.xiuxiu.confinement_nurse.ui.agency.vm;

import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.ui.order.vm.PageBean;

import java.io.Serializable;
import java.util.List;

public
class MechanismManBean implements Serializable {
    PageBean pageBean;
    List<UserInfoBean> items;

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<UserInfoBean> getItems() {
        return items;
    }

    public void setItems(List<UserInfoBean> items) {
        this.items = items;
    }


}
