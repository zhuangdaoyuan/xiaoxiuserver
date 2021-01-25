package com.xiuxiu.confinement_nurse.model.db.dao.user;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;


/**
 * 用户相关数据数据库
 */
//                    表名         数据库版本     不添加会警告
@Database(entities = {UserBean.class}, version = 3, exportSchema = false)
public abstract class UserDatabases extends RoomDatabase implements DatabaseInterface {
    abstract UserDao userDao();


    @Override
    public UserDaoInterface<UserBean> uerInterface() {
        return userDao();
    }

    @Override
    public RoomDatabase database() {
        return this;
    }
}

