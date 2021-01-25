package com.xiuxiu.confinement_nurse.model;

import androidx.annotation.StringDef;

import static com.xiuxiu.confinement_nurse.model.IntCodeEnum.KEY_HEALTH_CERT;
import static com.xiuxiu.confinement_nurse.model.IntCodeEnum.KEY_ID_CARD;
import static com.xiuxiu.confinement_nurse.model.IntCodeEnum.KEY_MATRON_CERT;
import static com.xiuxiu.confinement_nurse.model.IntCodeEnum.KEY_NO_CRIMINAL_CERT;

@StringDef({KEY_ID_CARD, IntCodeEnum.KEY_FACE_ID
        ,KEY_HEALTH_CERT
        ,KEY_NO_CRIMINAL_CERT
        ,KEY_MATRON_CERT
        , IntCodeEnum.KEY_NURSEMAID_CERT
})
public @interface IntCodeEnum {
    /**
     * 身份证"
     */
    String KEY_ID_CARD ="1";
    /**
     * 人脸识别
     */
    String KEY_FACE_ID ="2";
    /**
     * \健康证
     */
    String KEY_HEALTH_CERT ="3";
    /**
     * 无犯罪证明
     */
    String KEY_NO_CRIMINAL_CERT ="4";
    /**
     * 月嫂证
     */
    String KEY_MATRON_CERT ="5";
    /**
     * 育婴师证
     */
    String KEY_NURSEMAID_CERT ="6";
}
