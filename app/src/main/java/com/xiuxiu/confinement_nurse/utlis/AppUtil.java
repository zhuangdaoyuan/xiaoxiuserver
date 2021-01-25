package com.xiuxiu.confinement_nurse.utlis;

import android.app.ActivityManager;
import android.content.Context;

public final class AppUtil {
    private AppUtil() {
    }

    /**
     * 判断当前进程是否为主进程
     */
    public static boolean isUIProcess(Context context) {
        try {
            int pid = android.os.Process.myPid();
            String processName = "";
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    processName = appProcess.processName;
                    break;
                }
            }
            String packageName = context.getPackageName();
            return processName.equals(packageName);
        } catch (Throwable throwable) {
        }
        return false;
    }



}
