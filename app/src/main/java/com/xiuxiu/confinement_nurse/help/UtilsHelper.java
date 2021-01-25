package com.xiuxiu.confinement_nurse.help;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UtilsHelper {
    public static  boolean isPassword2(String password){
        int length = password.length();
        return length>=6&&length<=16;
    }
    public static  boolean isPassword(String password){
        String regex="^(?![0-9])(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(password);
        boolean isMatch=m.matches();
        return isMatch;
    }
    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            return IDCard.matches(IDCardRegex);
        }
        return false;
    }

}
