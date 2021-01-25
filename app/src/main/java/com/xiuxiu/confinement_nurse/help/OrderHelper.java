package com.xiuxiu.confinement_nurse.help;

import android.text.TextUtils;

public final class OrderHelper {
    /**
     * 是否可以投递
     */
    public static boolean canItBeDelivered(String type) {
        return TextUtils.equals(type, "0");
    }

    /**
     * 是否已经投递
     */
    public static boolean hasItBeenDelivered(String type) {
        return TextUtils.equals(type, "1");
    }

    /**
     * 用户类型
     *
     * @return
     */
    public static String userType(String type) {
        if (TextUtils.equals(type, String.valueOf(1))) {
            return "个人";
        }
        return "其他";
    }
}
