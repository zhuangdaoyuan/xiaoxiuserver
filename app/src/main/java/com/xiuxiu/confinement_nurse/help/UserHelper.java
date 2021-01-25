package com.xiuxiu.confinement_nurse.help;

import android.text.TextUtils;

import com.xiuxiu.confinement_nurse.Constants;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.CertificateInfoBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.event.LoginEvent;
import com.xiuxiu.confinement_nurse.utlis.ActivityUtils;
import com.xiuxiu.confinement_nurse.utlis.Utils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class UserHelper {


    /**
     * 月嫂 证件通过 状态认证通过
     */
    public static final int KEY_CERTIFIED = 1;

    public static final String KEY_DEFAULT_CITY_NAME = "北京";
    public static final String KEY_DEFAULT_CITY_CODE = "110000";
    
    /**
     * 游客id
     */
    public static final int KEY_VISITOR_ID = -1;
    public static final String DB_NAME_BY_VISITOR = "-1.db";

    /**
     * 月嫂订单状态
     * MatronOrderStatus
     */
//    待确认(0),
    public static final String KEY_MATRON_ORDER_STATUS_TO_BE_CONFIRMED = "0";
//    待上户
    public static final String KEY_MATRON_ORDER_STATUS_PENDING = "10";
//    进行中(
    public static final String KEY_MATRON_ORDER_STATUS_PROCESSING = "20";
//    已完成
    public static final String KEY_MATRON_ORDER_STATUS_COMPLETED= "30";
//    已结算
    public static final String KEY_MATRON_ORDER_STATUS_SETTLED= "40";
//    已取消
    public static final String KEY_MATRON_ORDER_STATUS_CANCELLED= "-10";

    public static Map<String, String> MatronOrderState = new TreeMap<String, String>() {{
        put(KEY_MATRON_ORDER_STATUS_TO_BE_CONFIRMED, "待确认");
        put(KEY_MATRON_ORDER_STATUS_PENDING, "待上户");
        put(KEY_MATRON_ORDER_STATUS_PROCESSING, "进行中");
        put(KEY_MATRON_ORDER_STATUS_COMPLETED, "已完成");
        put(KEY_MATRON_ORDER_STATUS_SETTLED, "已结算");
        put(KEY_MATRON_ORDER_STATUS_CANCELLED, "已取消");
    }};

//    未支付(0, UserOrderStatus.待付款,null),
    public static final String KEY_USER_ORDER_UNPAID = "0";
    //    已支付(1,UserOrderStatus.进行中,MatronOrderStatus.待确认),
    public static final String KEY_USER_ORDER_PAID = "1";
    //    已接单(10,UserOrderStatus.进行中,MatronOrderStatus.待上户),
    public static final String KEY_USER_ORDER_ORDERS= "10";
    //    已上户(20,UserOrderStatus.进行中,MatronOrderStatus.进行中),
    public static final String KEY_USER_ORDER_PROCESSING = "20";
//    申请完成(25,UserOrderStatus.进行中,MatronOrderStatus.进行中),
    public static final String KEY_USER_ORDER_APPLICATION_COMPLETE = "25";

//    已完成(30,UserOrderStatus.已完成,MatronOrderStatus.已完成),
    public static final String KEY_USER_ORDER_COMPLETE = "30";

//    已取消(-10,UserOrderStatus.已取消,MatronOrderStatus.已取消);
    public static final String KEY_USER_ORDER_CANCELLED = "-10";


    public static Map<String, String> orderByDeliveryType = new TreeMap<String, String>() {{
        put("-1", "不限");
        put("1", "已可投");
        put("2", "不可投");
    }};
    public static Map<String, String> orderByDeliveryState = new TreeMap<String, String>() {{
        put("0", "未投递");
        put("1", "已可投");
        put("2", "不可投");
    }};

    public static Map<String, String> orderByPriceRangeType = new TreeMap<String, String>() {{
        put("-1", "不限");
        put("1", "5K以下");
        put("2", "5-8K");
        put("3", "8-10K");
        put("4", "10-12K");
        put("5", ">12K");
    }};
    public static Map<String, String> orderByTimeState = new TreeMap<String, String>() {{
        put("-1", "不限");
        put("1", "1~7天");
        put("2", "7~15天");
        put("3", "15~30天");
        put("4", "30~60天");
        put("5", ">60天");
    }};


    public static Map<String, String> educationType = new TreeMap<String, String>() {{
        put("0", "其他");
        put("1", "小学");
        put("2", "初中");
        put("3", "高中");
        put("4", "中专");
        put("5", "专科");
        put("6", "本科");
        put("7", "研究生");
        put("8", "博士");
    }};

    public static Map<String, String> levelType = new TreeMap<String, String>() {{
        put("0", "其他");
        put("1", "一级");
        put("2", "二级");
        put("3", "三级");
        put("4", "四级");
        put("5", "五级");
        put("6", "初级");
        put("7", "中级");
        put("8", "高级");
        put("9", "特级");
    }};

    public static Map<String, String> serviceType = new TreeMap<String, String>() {{
        put("0", "其他");
        put("1", "月嫂");
        put("2", "育婴师");
        put("3", "母婴护理");
        put("4", "家政护理");
        put("5", "产后修复师");
        put("6", "催乳师");
        put("7", "早教师");
        put("8", "营养师");
        put("9", "餐饮科目");
    }};

    public static Map<String, String> myServiceType = new TreeMap<String, String>() {{
        put("-1","不限服务");
        put("0", "其他");
        put("1", "月嫂服务");
        put("2", "育婴师服务");
        put("3", "住院陪护");
        put("4", "母乳调理");
    }};
    public static Map<String, String> myServiceType2 = new TreeMap<String, String>() {{
        put("-1","不限");
        put("0", "其他");
        put("1", "月嫂");
        put("2", "育婴师");
        put("3", "住院陪护");
        put("4", "母乳调理");
    }};


    public static Map<String, String> specialService = new TreeMap<String, String>() {{
        put("-1", "不限");
        put("0", "无");
        put("1", "早产");
        put("2", "大小三阳");
        put("3", "双胞胎");
    }};
    public static Map<String, String> otherSkills = new TreeMap<String, String>() {{
        put("-1", "不限");
        put("0", "无");
        put("1", "宝宝辅食");
        put("2", "早教");
        put("3", "小儿推拿");
    }};

    public static Map<String, String> timeType = new TreeMap<String, String>() {{
        put("0", "未知");
        put("1", "全天");
        put("2", "仅白天");
        put("3", "仅晚上");
    }};


    public static void switchVisitor() {

        ModelManager.getInstance().switchDb(Utils.getApp(), String.valueOf(KEY_VISITOR_ID));
        ModelManager.getInstance().getUserInterface().updateUserBean(ModelManager.getInstance().getUserInterface().requestCurrentUserBean());
        EventBus.LogoutEvent().post(new LoginEvent());
    }

    public static void switchUser(UserBean userBean) {
        UserBean userBean1 = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
        if (TextUtils.equals(userBean1.getId(),userBean.getId())) {
            ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
            return;
        }
        ModelManager.getInstance().switchDb(Utils.getApp(), userBean.getId());
        ModelManager.getInstance().getUserInterface().insertUserBean(userBean);

        EventBus.LoginEvent().post(new LoginEvent());
    }

    public static boolean isLogin() {
        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
        return isLogin(userBean);
    }

    public static boolean isLogin(UserBean userBean) {
        if (userBean == null) {
            return false;
        }
        boolean b = !String.valueOf(KEY_VISITOR_ID).equals(userBean.getId());
        return b || !TextUtils.isEmpty(userBean.getToken());
    }

    /**
     * 是否认证通过
     *
     * @return
     */
    public static boolean whetherItIsCertified(CertificateInfoBean certificateInfoBean, String type) {
        if (certificateInfoBean == null) {
            return false;
        }
        List<CertificateInfoBean.CertificateInfo> items = certificateInfoBean.getItems();
        if (items == null || items.isEmpty()) {
            return false;
        }
        for (CertificateInfoBean.CertificateInfo c : items) {
            if (TextUtils.equals(c.getAuthType(), type)) {
                if (TextUtils.equals(c.getState(), String.valueOf(UserHelper.KEY_CERTIFIED))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void exit() {
        ActivityUtils.finishAllActivities();
        System.exit(0);
    }

    public static boolean isYuesao() {
        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
        return TextUtils.equals(userBean.getUserType(),Constants.KEY_USE);
    }
    public static boolean isJiGou() {
        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
        return TextUtils.equals(userBean.getUserType(), Constants.KEY_AGENCY);
    }
}
