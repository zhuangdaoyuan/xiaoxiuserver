package com.xiuxiu.confinement_nurse.model.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.activity.ActivityResultRequest;
import com.xiuxiu.confinement_nurse.ui.login.LoginActivity;
import com.xiuxiu.confinement_nurse.utlis.ActivityUtils;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_LOGIN_SUCCESS;
import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_NEED_LOGIN;

@Interceptor(priority = 8, name = "登录拦截器")
public class LoginInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        int extra = postcard.getExtra();
        if (extra == KEY_NEED_LOGIN) {
            if (UserHelper.isLogin()) {
                callback.onContinue(postcard);
            } else {
                Activity topActivity = ActivityUtils.getTopActivity();
                if (topActivity instanceof FragmentActivity) {
                    new ActivityResultRequest((FragmentActivity) topActivity)
                            .startForResult(new Intent(topActivity, LoginActivity.class), new ActivityResultRequest.Callback() {
                                @Override
                                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                    if (resultCode == KEY_LOGIN_SUCCESS) {
                                        callback.onContinue(postcard);
                                    } else {
                                        callback.onInterrupt(new LoginErrorException());
                                    }
                                }
                            });
                } else {
                    callback.onInterrupt(new NoFoundActivityException());
                }
            }
        }else{
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {

    }
}
