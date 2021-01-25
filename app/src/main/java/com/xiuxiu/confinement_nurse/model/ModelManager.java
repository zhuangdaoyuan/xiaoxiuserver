package com.xiuxiu.confinement_nurse.model;

import android.content.Context;

import com.xiuxiu.confinement_nurse.model.cache.CacheInterface;
import com.xiuxiu.confinement_nurse.model.db.UserInterface;
import com.xiuxiu.confinement_nurse.model.db.UserInterfaceImpl;
import com.xiuxiu.confinement_nurse.model.db.dao.DaoInterface;
import com.xiuxiu.confinement_nurse.model.db.dao.DaoInterfaceImpl;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;

public class ModelManager {
    private CacheInterface cacheInterface;
    private UserInterface userInterface;
    private XFunc0 mSwitchUserCallBack;
    /**
     * 查询用户信息出错的处理。
     */
    private XFunc0 mFindUserDateByErrorCallBack;

    private DaoInterface mDaoInterface;

    private ModelManager() {
    }

    public static ModelManager getInstance() {
        return ModeManagers.modeManager;
    }

    private static class ModeManagers {
        static ModelManager modeManager = new ModelManager();
    }



    public  void init(Context context){
        mDaoInterface = new DaoInterfaceImpl();
        cacheInterface = new CacheInterface();

        /**
         * 先设置监听在初始化
         */
        mDaoInterface.switchDaoCallBack(new XFunc2<String, Boolean>() {
            @Override
            public void call(String id, Boolean isVisitor) {
                userInterface = new UserInterfaceImpl(id, mDaoInterface.userDaoInterface(), new XFunc0() {
                    @Override
                    public void call() {
                        //用户信息发生改变 但是可能回调多次
                        if (mSwitchUserCallBack != null) {
                            mSwitchUserCallBack.call();
                        }
                    }
                });
                /**
                 * 初始化顺序不能乱
                 */
                userInterface.init(isVisitor);
            }
        });
        mDaoInterface.init(context);
    }


    public void switchDb(Context context, String id) {
        mDaoInterface.switchDb(context, id, id + ".db");
    }











    public UserInterface getUserInterface() {
        return userInterface;
    }


    public CacheInterface getCacheInterface() {
        return cacheInterface;
    }

    /**
     * 当前用户信息查询失败的处理
     *
     * @param mFindUserDateByErrorCallBack
     */
    public void setFindUserDateByErrorCallBack(XFunc0 mFindUserDateByErrorCallBack) {
        this.mFindUserDateByErrorCallBack = mFindUserDateByErrorCallBack;
    }

    public XFunc0 getFindUserDateByErrorCallBack() {
        return mFindUserDateByErrorCallBack;
    }
    public void setSwitchUserCallBack(XFunc0 mSwitchUserCallBack) {
        this.mSwitchUserCallBack = mSwitchUserCallBack;
    }
}
