package com.xiuxiu.confinement_nurse.model;

import com.xiuxiu.confinement_nurse.ui.WelcomeActivity;

import rxhttp.wrapper.annotation.DefaultDomain;

public final class Configuration {
    @DefaultDomain() //设置为默认域名
//    public static String baseUrl = "http://139.159.243.117:13000";
    public static String baseUrl = "https://www.xiaoxiuapp.com";





    public static final String KEY_CONTACT_US = "/api/common/contact-us";


    /////////login//////////////
    public static final String KEY_LOGIN = "/api/login/login";

    //机构登录
    public static final String KEY_LOGIN_MECHANISM = "/api/cases/org/login";
//检查机构审核状态
    public static final String KEY_MECHANISM_STATE = "/api/cases/org/flow";



    public static final String KEY_LOGOUT = "/api/login/logout";

    /**
     * 验证码
     */
    public static final String KEY_REGISTER_CODE = "/api/login/register/code";
    /**
     * 注册
     */
    public static final String KEY_REGISTER = "/api/login/register";

    public static final String KEY_PHONE = "/api/login/change/phone";

    /**
     * 重置密码的验证码
     */
    public static final String KEY_RESET_PASSWORD_CODE = "/api/login/reset/password/code";


    public static final String KEY_RESET_PASSWORD_CHECK = "/api/login/reset/password/code/check";
    /**
     * 重置密码(手机号方式)
     */
    public static final String KEY_RESET_PASSWORD_BY_PHONE = "/api/login/reset/password/by-phone";
    public static final String KEY_RESET_PASSWORD_BY_PASSWORD = "/api/login/reset/password/by-pwd";
    public static final String KEY_RESET_PHONE= "/api/login/change/phone/code";


    /**
     * 文件上传地址
     */
    public static final String KEY_UPLOAD = "/file/upload";

    public  static class User{
        public static final String KEY_TEAM_INFORMATION = "/api/matron/matron-group/group-info";
        public static final String KEY_JOIN_CODE = "/api/matron/matron-group/join";
        public static final String KEY_INVITATION_CODE = "/api/matron/matron-group/my/code";
        public static final String KEY_STATE_INFO = "/api/matron/stat-info";

        /**
         * 用户信息获取 和上报
         */
        public static final String KEY_USER_INFO = "/api/matron/matron-info";
        /**
         * 获取认证信息
         */
        public static final String KEY_CERTIFICATE_INFO = "/api/matron/certification/certificate-info";
        /**
         * 更新认证信息
         */
        public static final String KEY_CERTIFICATE_UPLOAD_INFO = "/api/matron/certification/upload-info";


        public static final String KEY_EDUCATION_GET = "/api/matron/education/list";

        public static final String KEY_ORDER_INFO_GET = "/api/matron/order-info/finished";

        /**
         * 获取其他经历
         */
        public static final String KEY_OTHER_GET = "/api/matron/other-experience/list";

        public static final String KEY_OTHER_ADD = "/api/matron/other-experience/add";
        public static final String KEY_OTHER_DECTED = "/api/matron/other-experience/delete";
        public static final String KEY_OTHER_UPDATE = "/api/matron/other-experience/update";

        public static final String KEY_SELF_TERMINATION_GET = "/api/matron/introduce";

       //新增学习教育经历
        public static final String KEY_EDUCATION_ADD = "/api/matron/education/add";
        public static final String KEY_EDUCATION_UPDATE = "/api/matron/education/update";

        public static final String KEY_EDUCATION_DELETE= "/api/matron/education/delete";

        public static final String KEY_SERVICE_LiST_GET = "/api/matron/service/list";
        public static final String KEY_SERVICE_LIST_ADD = "/api/matron/service/add";
        public static final String KEY_SERVICE_LIST_UPDATE = "/api/matron/service/update";
        public static final String KEY_SERVICE_LIST_DELETE = "/api/matron/service/delete";

    }
    public static class Location{
        public static final String KEY_LOCATION = "/api/location/locations";
        public static final String KEY_CITY= "/api/location/city";
    }
    public static class Office{
        public static final String KEY_RECOMMEND = "/api/matron/employment-search/recommend";

        public static final String KEY_NEARBY = "/api/matron/employment-search/nearby";

        public static final String KEY_LATEST = "/api/matron/employment-search/latest";

        /**
         * 投递简历
         */
        public static final String KEY_POST_RESUME = "/api/matron/resume-delivery/add";

        public static final String KEY_POST_DETAIL = "/api/matron/employment/detail";

    }
    public static class Order{
        //订单列表
        public static final String KEY_ORDER_LIST = "/api/matron/order-info/list";
        //确认订单
        public static final String KEY_ORDER_CONFIRM= "/api/matron/order-info/ensure";
        //订单详情
        public static final String KEY_ORDER_DETAIL= "/api/matron/order-info/order-detail";
        //申请完成
        public static final String KEY_ORDER_APPLY_FINISH= "/api/matron/order-info//apply-finish";
        //上户
        public static final String KEY_ORDER_ON_THE_DOOR= "/api/matron/order-info/on-the-door";
        //合同
        public static final String KEY_ORDER_CONTRACT= "/api/matron/order-info/contract-detail";



    }

    public static class Schedule{
        public static final String KEY_SCHEDULE_LIST =  "/api/matron/schedule/list";
        public static final String KEY_SCHEDULE_ADD =  "/api/matron/schedule/add";
        public static final String KEY_SCHEDULE_DELETE =  "/api/matron/schedule/delete";
    }
    public static class News{
        public static final String KEY_NEWS_TOKEN="/rongyun/token";
        public static final String KEY_USE_INFO="/rongyun/user-info/list";
    }

    public static class mechanism{
        //机构注册
        public static final String KEY_JG_REGISTER="/api/cases/org/register";
        //机构重新提交
        public static final String KEY_JG_REGISTER1="/api/cases/org/orgSub";
        public static final String KEY_JG_INFO="/api/cases/org/info";
        //查询重新提交的信息
        public static final String KEY_TY_RECODE="/api/cases/ty/recode";

        public static final String KEY_YUESAO_LIST = "/api/cases/org/ysSearch";

        public static final String KEY_YUESAO_INFO = "/api/cases/org/ysSaveOrUpdate";
        //机构下的月嫂订单列表
        public static final String KEY_ORDER_LIST = "api/cases/org/ys/order/list";

        //机构的订单列表
        public static final String KEY_ORDER_LIST_All = "api/cases/org/ys/order/list/all";

        public static final String KEY_ORDER_CONFIRM= "/api/cases/org/ys/order/ensure";
        //上户
        public static final String KEY_ORDER_ON_THE_DOOR= "/api/cases/org/ys/order/on-the-door";
        //申请完成
        public static final String KEY_ORDER_APPLY_FINISH= "/api/cases/org/ys/order/apply-finish";
        //订单详情
        public static final String KEY_ORDER_DETAIL= "/api/cases/org/ys/order/order-detail";
        //合同
        public static final String KEY_ORDER_CONTRACT= "/api/cases/org/ys/order/order-detail";


        //档期列表
        public static final String KEY_SCHEDULE_LIST =  "/api/cases/org/ys/schedule/list";
        //档期删除
        public static final String KEY_SCHEDULE_DELETE =  "/api/cases/org/ys/schedule/delete";
        //档期增加
        public static final String KEY_SCHEDULE_ADD =  "/api/cases/org/ys/schedule/add";


        //新增学习教育经历
        public static final String KEY_EDUCATION_ADD = "/api/cases/org/ys/education/add";
        public static final String KEY_EDUCATION_DELETE= "/api/cases/org/ys/education/delete";
        public static final String KEY_EDUCATION_UPDATE = "/api/cases/org/ys/education/update";


        public static final String KEY_SERVICE_LiST_GET = "/api/cases/org/ys/service/list";
        public static final String KEY_SERVICE_LIST_ADD = "/api/cases/org/ys/service/add";
        public static final String KEY_SERVICE_LIST_UPDATE = "/api/cases/org/ys/service/update";
        public static final String KEY_SERVICE_LIST_DELETE = "/api/cases/org/ys/service/delete";

        public static final String KEY_EDUCATION_GET = "/api/cases/org/ys/education/list";

        public static final String KEY_ORDER_INFO_GET = "/api/cases/org/ys/order-info/finished";
        public static final String KEY_OTHER_GET = "/api/cases/org/ys/other-experience/list";
        public static final String KEY_SELF_TERMINATION_GET = "/api/cases/org/ys/introduce";


        public static final String KEY_SELF_TERMINATION_POST = "/api/cases/org/ys/introduce/add";


        public static final String KEY_CERTIFICATE_INFO = "/api/cases/org/ys/certification/certificate-info";

        public static final String KEY_CERTIFICATE_UPLOAD_INFO = "/api/cases/org/ys/certification/upload-info";
        public static final String KEY_OTHER_ADD = "/api/cases/org/ys/other-experience/add";
        public static final String KEY_OTHER_UPDATE = "/api/cases/org/ys/other-experience/update";
        public static final String KEY_OTHER_DECTED = "/api/cases/org/ys/other-experience/delete";



    }

}
