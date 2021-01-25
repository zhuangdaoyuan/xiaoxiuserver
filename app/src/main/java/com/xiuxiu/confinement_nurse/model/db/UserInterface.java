package com.xiuxiu.confinement_nurse.model.db;

import androidx.lifecycle.LiveData;

import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;


public interface UserInterface {
    void init(Boolean aBoolean);

    /**
     * 获取当前用户每次都是从数据库中取值，
     * 有可能会出错，出错了需要重新登录初始化
     *
     * @return
     */
    public boolean requestCurrentUserBean(XFunc1<UserBean> callback);

    /**
     * 获取当前用户 缓存信息
     *
     * @return
     */
    public UserBean requestCurrentUserBean();

    public LiveData<UserBean> requestCurrentUserBeanByLive();

    /**
     * 保存登录用户信息
     *
     * @param userBean
     */
    public void insertUserBean(UserBean userBean);

    public void updateUserBean(UserBean userBean);


}
