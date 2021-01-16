package com.monster.share;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import com.monster.share.callback.SocialLoginCallback;
import com.monster.share.callback.SocialShareCallback;
import com.monster.share.entities.ShareEntity;
import com.monster.share.entities.ThirdInfoEntity;

/**
 * Created by arvinljw on 17/11/27 15:58
 * Function：
 * Desc：
 */
final class WBHelper implements ISocial, INeedLoginResult {
    public WBHelper(Activity activity, String wbAppId, String wbRedirectUrl) {
    }

    @Override
    public void setNeedLoginResult(boolean needLoginResult) {

    }

    @Override
    public boolean isNeedLoginResult() {
        return false;
    }

    @Override
    public void login(SocialLoginCallback callback) {

    }

    @Override
    public ThirdInfoEntity createThirdInfo() {
        return null;
    }

    @Override
    public void share(SocialShareCallback callback, ShareEntity shareInfo) {

    }

    @Override
    public void onDestroy() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
