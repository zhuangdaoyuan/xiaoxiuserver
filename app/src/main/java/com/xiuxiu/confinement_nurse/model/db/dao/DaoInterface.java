package com.xiuxiu.confinement_nurse.model.db.dao;

import android.content.Context;

import com.xiuxiu.confinement_nurse.model.db.dao.user.UserDaoInterface;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;


/**
 * 对外提供操作数据库的地方。
 */
public interface DaoInterface {
    //////////////////初始化操作////////////////////////////////////
    public void init(Context context);

    public void switchDb(Context context, String id, String dbname);


    /////////////////////////用户相关操作 ，操作对象来源于初始化/切换后的 RoomDatabase///////////////////////////////////////////////////////////////


    //////////////////////////游客/////////////////////////////////////////////////////////////////////////////////////


    ///////////////////播放列表操作/////////////////////////////////////////////////

    public UserDaoInterface<UserBean> userDaoInterface();



    /**
     * @param xFunc0 是否是游客的数据库
     */
     void switchDaoCallBack(XFunc2<String, Boolean> xFunc0);
}
