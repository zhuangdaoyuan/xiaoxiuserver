package com.monster.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.monster.share.callback.SocialCallback;
import com.monster.share.callback.SocialLoginCallback;
import com.monster.share.callback.SocialShareCallback;
import com.monster.share.entities.QQInfoEntity;
import com.monster.share.entities.QQLoginResultEntity;
import com.monster.share.entities.QQUnionIdEntity;
import com.monster.share.entities.ShareEntity;
import com.monster.share.entities.ThirdInfoEntity;


/**
 * Created by arvinljw on 17/11/24 15:59
 * Function：
 * Desc：
 */
final class QQHelper implements ISocial, INeedLoginResult {
    public QQHelper(Activity activity, String qqAppId) {
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
