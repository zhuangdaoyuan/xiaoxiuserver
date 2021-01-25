package com.xiuxiu.confinement_nurse.model.db.dao.user;

import androidx.lifecycle.LiveData;


import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;

import java.util.List;

public interface UserDaoInterface<T extends UserBean> {
    public List<T> loadAllUsers();

    public T loadUserById(String id);

    public LiveData<T> getUserById(String id);

    public void updateUsers(T... users);

    void insertUsers(UserBean... userBeans);
}
