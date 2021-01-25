package com.xiuxiu.confinement_nurse.help;


import com.xiuxiu.confinement_nurse.utlis.HandlerUtil;
import com.xiuxiu.confinement_nurse.utlis.ToastUtils;
import com.xiuxiu.confinement_nurse.utlis.Utils;

/**
 * toast 提示工具类
 *
 * @author zhoulei
 */

public class ToastHelper {
    public static void showToast(final String message) {
        if (HandlerUtil.isMainThread()) {
           ToastUtils.showShort(message);
        }else{
            Utils.runOnUiThread(() ->ToastUtils.showShort(message));
        }
    }
}
