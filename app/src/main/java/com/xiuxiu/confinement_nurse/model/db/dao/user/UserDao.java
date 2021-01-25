package com.xiuxiu.confinement_nurse.model.db.dao.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;

import java.util.List;


/**
 * 用户相关操作
 */
@Dao
public interface UserDao extends UserDaoInterface<UserBean> {
    /////////////////////  信息查询///////////////////////////////////////
    @Query("SELECT * FROM user")
    @Override
    public List<UserBean> loadAllUsers();

    @Query("SELECT * FROM user WHERE id = :id")
    @Override
    public UserBean loadUserById(String id);


    @Query("SELECT * FROM user WHERE id = :id")
    @Override
    public LiveData<UserBean> getUserById(String id);

    /////////////////////更新////////////////////////////////////////////////
    @Update
    @Override
    public void updateUsers(UserBean... users);

    @Insert
    @Override
    void insertUsers(UserBean... userBeans);




    /////////////////////////////////////////////历史记录////////////////////////////////////////////////////////////////////


}
