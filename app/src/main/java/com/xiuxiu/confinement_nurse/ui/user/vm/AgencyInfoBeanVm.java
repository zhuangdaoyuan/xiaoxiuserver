package com.xiuxiu.confinement_nurse.ui.user.vm;

import com.xiuxiu.confinement_nurse.model.db.pojo.AgencyInfoBean;

import java.io.Serializable;

public class AgencyInfoBeanVm implements Serializable {
    private AgencyInfoBean data;

    public AgencyInfoBean getData() {
        return data;
    }

    public void setData(AgencyInfoBean data) {
        this.data = data;
    }
}
