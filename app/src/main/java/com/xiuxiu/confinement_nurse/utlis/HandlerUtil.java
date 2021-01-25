package com.xiuxiu.confinement_nurse.utlis;

import android.os.Handler;
import android.os.Looper;

/**
 * 全局handler 使用一个
 */
public class HandlerUtil {
    public static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        HANDLER.post(runnable);
    }

    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

    public static void removeRunnable(Runnable runnable) {
        HANDLER.removeCallbacks(runnable);
    }


    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
