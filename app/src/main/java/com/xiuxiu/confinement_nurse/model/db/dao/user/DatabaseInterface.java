package com.xiuxiu.confinement_nurse.model.db.dao.user;

import androidx.room.RoomDatabase;

import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;


/**
 * 同步不同数据库直接的差异
 */
public interface DatabaseInterface {

    public UserDaoInterface<UserBean> uerInterface();
    public RoomDatabase database();
}
