package com.monster.share;


import com.monster.share.callback.SocialLoginCallback;
import com.monster.share.callback.SocialShareCallback;
import com.monster.share.entities.ShareEntity;
import com.monster.share.entities.ThirdInfoEntity;

/**
 * Created by arvinljw on 17/11/24 16:06
 * Function：
 * Desc：
 */
public interface ISocial {
    void login(SocialLoginCallback callback);

    ThirdInfoEntity createThirdInfo();

    void share(SocialShareCallback callback, ShareEntity shareInfo);

    void onDestroy();
}
