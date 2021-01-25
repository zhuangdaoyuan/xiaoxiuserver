package com.xiuxiu.confinement_nurse.wxapi;

import com.monster.share.SocialHelper;
import com.monster.share.WXHelperActivity;
import com.xiuxiu.confinement_nurse.utlis.SocialUtil;

public class WXEntryActivity extends WXHelperActivity {

    @Override
    protected SocialHelper getSocialHelper() {
        return SocialUtil.INSTANCE.socialHelper;
    }
}
