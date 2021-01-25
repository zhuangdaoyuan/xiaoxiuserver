package com.xiuxiu.confinement_nurse.model.db;

/**
 * 播放中出现的异常
 */
public class UserDataException extends Exception {
    public UserDataException() {
        super("用户数据异常，请重新登录,需要执行登出操作");
    }
}
