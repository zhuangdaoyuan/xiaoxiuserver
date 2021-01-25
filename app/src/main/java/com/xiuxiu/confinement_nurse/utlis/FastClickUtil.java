package com.xiuxiu.confinement_nurse.utlis;

import android.view.View;

import com.xiuxiu.confinement_nurse.R;

/**
 * @author :庄道园
 * Date :2019/12/23
 * 安静撸码，淡定做人
 */
public class FastClickUtil {

    // 两次点击间隔不能少300ms 往大调也可以
    private static final int MIN_DELAY_TIME =300;
    private static long lastClickTime;

    /**
     *
     * @return false 是２次点击间隔在 MIN_DELAY_TIME以上
     */
    public static boolean isFastClick() { //这个方法可以放到公共类里
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public static boolean isFastClick(View view) { //这个方法可以放到公共类里
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        Object tag = view.getTag(R.id.fast_click);
        if (tag!=null) {
            if (tag instanceof Long) {
                if ((currentClickTime -(Long) tag) >= 200) {
                    flag = false;
                }
            }
        }
        view.setTag(R.id.fast_click,currentClickTime);
        return flag;
    }
}
