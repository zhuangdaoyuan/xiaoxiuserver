package com.xiuxiu.confinement_nurse.utlis;

import android.util.Log;

/**
 * 日志处理工具类
 */
public class LogUtils {


    private static boolean sIsDebug = true;

    /**
     * 设置调试模式，调试模式打印日志，非调试模式，只打印error级别日志
     *
     * @param isDebug true:debug 模式，false：生产环境
     */
    public static void setDebug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    /**
     * 打印错误级别日志
     *
     * @param tag     名字
     * @param message 内容
     */
    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    /**
     * 打印调试级别日志，生成环境无日志
     *
     * @param tag     名字
     * @param message 内容
     */
    public static void d(String tag, String message) {
        if (sIsDebug) {
            Log.d(tag, message);
        }
    }

    /**
     * 打印info日志，生成环境无日志
     *
     * @param tag     名字
     * @param message 内容
     */
    public static void i(String tag, String message) {
        if (sIsDebug) {
            Log.i(tag, message);
        }
    }

    /**
     * 打印v日志，生成环境无日志
     *
     * @param tag     名字
     * @param message 内容
     */
    public static void v(String tag, String message) {
        if (sIsDebug) {
            Log.v(tag, message);
        }
    }

    /**
     * 打印警告日志，生成环境无日志
     *
     * @param tag     名字
     * @param message 内容
     */
    public static void w(String tag, String message) {
        if (sIsDebug) {
            Log.w(tag, message);
        }
    }
}
