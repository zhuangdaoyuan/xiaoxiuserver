package com.xiuxiu.confinement_nurse.model.db;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.dao.user.UserDaoInterface;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.List;

public class UserInterfaceImpl implements UserInterface {
    UserDaoInterface<UserBean> userDaoInterface;

    UserBean userBean;
    private String userId;
    private XFunc0 mUpdateUserCallBack;

    public UserInterfaceImpl(String id, UserDaoInterface<UserBean> userDaoInterface, @NonNull XFunc0 mUpdateUserCallBack) {
        this.userId = id;
        this.userDaoInterface = userDaoInterface;
        this.mUpdateUserCallBack = mUpdateUserCallBack;
    }

    @Override
    public void init(Boolean isVisitor) {
        if (userDaoInterface.loadAllUsers().size() == 0) {
            UserBean visitorBean = new UserBean(userId);
            visitorBean.setName(isVisitor ? "游客" : String.valueOf(userId));
            insertUserBean(visitorBean);
            userBean = visitorBean;
        } else {
            userBean = userDaoInterface.loadUserById(userId);
            if (userBean == null) {
                if (ModelManager.getInstance().getFindUserDateByErrorCallBack() != null) {
                    ModelManager.getInstance().getFindUserDateByErrorCallBack().call();
                }
            } else if (isVisitor) {
                userBean.setToken("");
                updateUserBean(userBean);
            }
        }
    }

    @Override
    public boolean requestCurrentUserBean(XFunc1<UserBean> callback) {
        try {
            callback.call(getCurrentUserBean2());
            return true;
        } catch (UserDataException e) {
            if (ModelManager.getInstance().getFindUserDateByErrorCallBack() != null) {
                ModelManager.getInstance().getFindUserDateByErrorCallBack().call();
            }
        }
        return false;
    }

    @Override
    @NonNull
    public UserBean requestCurrentUserBean() {
        if (userBean==null) {
            UserBean visitorBean = new UserBean("-1");
            visitorBean.setName("游客");
            insertUserBean(visitorBean);
            userBean=visitorBean;
        }
        return userBean;
    }

    @Override
    @NonNull
    public LiveData<UserBean> requestCurrentUserBeanByLive() {
        String id = ModelManager.getInstance().getCacheInterface().getUserId();
        return userDaoInterface.getUserById(id);
    }

    @NonNull
    private UserBean getCurrentUserBean2() throws UserDataException {
        String id = ModelManager.getInstance().getCacheInterface().getUserId();
        if (!TextUtils.isEmpty(id)) {
            UserBean userBean = userDaoInterface.loadUserById(id);
            if (userBean != null) {
                return userBean;
            }
        } else {
            List<UserBean> userBeans = userDaoInterface.loadAllUsers();
            if (userBeans.size() != 0) {
                return userBeans.get(0);
            }
        }
        throw new UserDataException();
    }


    @Override
    public void insertUserBean(UserBean userBean) {
        UserBean userBean1 = userDaoInterface.loadUserById(userBean.getId());
        if (userBean1 == null) {
            userDaoInterface.insertUsers(userBean);
            this.userBean = userBean;
            ModelManager.getInstance().getCacheInterface().saveUserId(String.valueOf(userBean.getId()));
        } else {
            updateUserBean(userBean);
        }
        if (mUpdateUserCallBack != null) {
            mUpdateUserCallBack.call();
        }
    }

    @Override
    public void updateUserBean(UserBean userBean) {

        userDaoInterface.updateUsers(userBean);
        this.userBean = userBean;
        ModelManager.getInstance().getCacheInterface().saveUserId(String.valueOf(userBean.getId()));

        if (mUpdateUserCallBack != null) {
            mUpdateUserCallBack.call();
        }
    }
}
