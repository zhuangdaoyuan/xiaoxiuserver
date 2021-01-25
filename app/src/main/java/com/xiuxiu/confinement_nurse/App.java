package com.xiuxiu.confinement_nurse;

import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.SDKInitializer;
import com.monster.gamma.callback.LayoutEmpty;
import com.monster.gamma.callback.LayoutError;
import com.monster.gamma.callback.LayoutLoading;
import com.monster.gamma.callback.LayoutNoLogin;
import com.monster.gamma.callback.LayoutNoOrder;
import com.monster.gamma.core.Gamma;
import com.xiuxiu.confinement_nurse.help.RongYunHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;
import com.xiuxiu.confinement_nurse.utlis.AppUtil;
import com.xiuxiu.confinement_nurse.utlis.Utils;
import com.xiuxiu.confinement_nurse.help.location.LocationService;

import java.util.LinkedHashMap;

import rxhttp.HttpSender;
import rxhttp.wrapper.callback.Function;
import rxhttp.wrapper.param.Method;
import rxhttp.wrapper.param.Param;
import rxhttp.wrapper.param.RxHttp;

public class App extends MultiDexApplication {
    public LocationService locationService;
    public LinkedHashMap<String, City> linkedHashMap;

    @Override
    public void onCreate() {
        super.onCreate();

        if (AppUtil.isUIProcess(this)) {
            Utils.init(this);
            ModelManager.getInstance().init(this);
            Gamma
                    .beginBuilder()
                    .addCallback(new LayoutLoading())
                    .addCallback(new LayoutNoLogin())
                    .addCallback(new LayoutError())
                    .addCallback(new LayoutNoOrder())
                    .addCallback(new LayoutEmpty())
                    .setDefaultCallback(LayoutLoading.class)
                    .commit();

            if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
                ARouter.openLog();     // 打印日志
                ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            }
            ARouter.init(this); // 尽可能早，推荐在Application中初始化


            initHttp();
            initImageSelect();

//            RxLocation.initialize(this);
            locationService = new LocationService(getApplicationContext());
            linkedHashMap = new LinkedHashMap<>();

            SDKInitializer.initialize(this);
        }

        RongYunHelper.init(this);

    }

    private void initImageSelect() {

    }


    private void initHttp() {
        //设置debug模式，默认为false，设置为true后，发请求，过滤"RxHttp"能看到请求日志
        RxHttp.setDebug(true);
        RxHttp.setOnParamAssembly(new Function<Param, Param>() {
            @Override
            public Param apply(Param p) { //此方法在子线程中执行，即请求发起线程
                Method method = p.getMethod();
                if (method.isGet()) {     //可根据请求类型添加不同的参数
                } else if (method.isPost()) {
                }
                if (UserHelper.isLogin()) {
                    UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                    p.addHeader("userType", userBean.getUserType());
                    p.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    p.addHeader("xtoken", userBean.getToken());
                }
                return p;
            }
        });
    }
}
