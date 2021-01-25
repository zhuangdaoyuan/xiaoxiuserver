package com.xiuxiu.confinement_nurse.ui.service;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityEditServerBinding;
import com.xiuxiu.confinement_nurse.databinding.ActivityMyServerBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.model.event.ServiceEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.SpacesItemDecoration;
import com.xiuxiu.confinement_nurse.ui.service.adapter.MyServerAdapter;
import com.xiuxiu.confinement_nurse.ui.service.vm.MyServerInfoVm;
import com.xiuxiu.confinement_nurse.ui.view.SwitchButton;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;

import java.util.List;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_NEED_LOGIN;

/**
 * 我的服务
 */
@Route(path = RouterHelper.KEY_EDIT_SERVICE, extras = KEY_NEED_LOGIN)
public class EditServiceActivity extends AbsBaseActivity implements MyServiceContract.IView {

    private MyServerInfoVm myServerInfoVm;

    private String ysId;
    public static void start(MyServerInfoVm myServerInfoVm) {
        Postcard build = ARouter.getInstance().build(RouterHelper.KEY_EDIT_SERVICE);
        if (myServerInfoVm != null) {
            build.withSerializable("data", myServerInfoVm);
        }
        build.navigation();
    }

    public static void start(MyServerInfoVm myServerInfoVm,String ysId) {
        Postcard build = ARouter.getInstance().build(RouterHelper.KEY_EDIT_SERVICE);
        if (myServerInfoVm != null) {
            build.withSerializable("data", myServerInfoVm);
        }
        if (!TextUtils.isEmpty(ysId)) {
            build.withString("id",ysId);
        }
        build.navigation();
    }

    private ActivityEditServerBinding inflate;
    private MyServiceContract.IPresenter presenter;

    @Override
    protected View getLayoutView() {
        inflate = ActivityEditServerBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRequestPageSuccess();
        initView();
        ysId=getIntent().getStringExtra("id");
        myServerInfoVm = (MyServerInfoVm) getIntent().getSerializableExtra("data");
        if (myServerInfoVm == null) {
            myServerInfoVm = new MyServerInfoVm();
        }
        initViewState();
        setListener();
        loadData();

    }

    private void loadData() {
        String serviceType = myServerInfoVm.getServiceType();
        switchServiceState(serviceType);
        switchTimeServiceState(myServerInfoVm.getTimeType());
        switchOtherServiceState(myServerInfoVm.getOtherSkills());
        switchSpecialServiceState(myServerInfoVm.getSpecialCare());

        inflate.heavenNumberActivityAddMyService.setSubTitleRight(myServerInfoVm.getDays());
        inflate.priceActivityAddMyService.setSubTitleRight(myServerInfoVm.getTotalPrice());

    }

    private void setListener() {
        for (int i = 0; i < inflate.llActivityEditYuyingshi2.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.llActivityEditYuyingshi2.getChildAt(i);
            childAt.setOnClickListener(v -> switchTimeServiceState(String.valueOf(v.getTag())));
        }
        for (int i = 0; i < inflate.llActivityEditYuyingshi4.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.llActivityEditYuyingshi4.getChildAt(i);
            childAt.setOnClickListener(v -> switchOtherServiceState(String.valueOf(v.getTag())));
        }
        for (int i = 0; i < inflate.llActivityEditYuesao2.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.llActivityEditYuesao2.getChildAt(i);
            childAt.setOnClickListener(v -> {
                SwitchButton childAt2 = (SwitchButton) v;
                childAt2.setButtonSelectState(!childAt2.isSelect());
            });
        }
        for (int i = 0; i < inflate.llActivityEditServiceType.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.llActivityEditServiceType.getChildAt(i);
            childAt.setOnClickListener(v -> switchServiceState(String.valueOf(v.getTag())));
        }
    }


    private void initViewState() {
        presenter = new MyServicePresenter(this);
        presenter.setYsId(ysId);
        String serviceType = myServerInfoVm.getServiceType();
        if (TextUtils.isEmpty(serviceType)) {
            inflate.title.setText("添加服务");
        } else {
            String s = UserHelper.myServiceType.get(serviceType);
            inflate.title.setText(s);
        }


    }

    private void initView() {
    }

    @Override
    public void onRequestMyServiceList(List<MyServerInfoVm> s) {

    }

    @Override
    public void onRequestDeleteSuccess(int position) {

    }


    private void switchTimeServiceState(String type) {
        myServerInfoVm.setTimeType(type);
        for (int i = 0; i < inflate.llActivityEditYuyingshi2.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.llActivityEditYuyingshi2.getChildAt(i);
            String tag = (String) childAt.getTag();
            if (TextUtils.equals(tag, type)) {
                childAt.setButtonSelectState(true);
            } else {
                childAt.setButtonSelectState(false);
            }
        }
    }


    private void switchOtherServiceState(String type) {
        myServerInfoVm.setOtherSkills(type);
        for (int i = 0; i < inflate.llActivityEditYuyingshi4.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.llActivityEditYuyingshi4.getChildAt(i);
            String tag = (String) childAt.getTag();
            if (TextUtils.equals(tag, type)) {
                childAt.setButtonSelectState(true);
            } else {
                childAt.setButtonSelectState(false);
            }
        }
    }


    private void switchSpecialServiceState(String type) {
        if (TextUtils.isEmpty(type)) {
            for (int i = 0; i < inflate.llActivityEditYuesao2.getChildCount(); i++) {
                SwitchButton childAt = (SwitchButton) inflate.llActivityEditYuesao2.getChildAt(i);
                childAt.setButtonSelectState(false);
            }
            return;
        }
        myServerInfoVm.setSpecialCare(type);
        String[] split = type.split(",");
        for (int i = 0; i < inflate.llActivityEditYuesao2.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.llActivityEditYuesao2.getChildAt(i);
            String tag = (String) childAt.getTag();
            for (int j = 0; j < split.length; j++) {
                String s = split[j];
                if (TextUtils.equals(tag, s)) {
                    childAt.setButtonSelectState(true);
                }
            }
        }
    }

    private void switchServiceState(String type) {
        myServerInfoVm.setServiceType(type);
        for (int i = 0; i < inflate.llActivityEditServiceType.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.llActivityEditServiceType.getChildAt(i);
            String tag = (String) childAt.getTag();
            if (TextUtils.equals(tag, type)) {
                childAt.setButtonSelectState(true);
            } else {
                childAt.setButtonSelectState(false);
            }
        }

        if (TextUtils.equals(type, "2")) {
            ViewHelper.hideView(inflate.llActivityEditYuesao1);
            ViewHelper.hideView(inflate.llActivityEditYuesao2);

            ViewHelper.showView(inflate.llActivityEditYuyingshi1);
            ViewHelper.showView(inflate.llActivityEditYuyingshi2);
            ViewHelper.showView(inflate.llActivityEditYuyingshi3);
            ViewHelper.showView(inflate.llActivityEditYuyingshi4);
        } else {
            ViewHelper.showView(inflate.llActivityEditYuesao1);
            ViewHelper.showView(inflate.llActivityEditYuesao2);

            ViewHelper.hideView(inflate.llActivityEditYuyingshi1);
            ViewHelper.hideView(inflate.llActivityEditYuyingshi2);
            ViewHelper.hideView(inflate.llActivityEditYuyingshi3);
            ViewHelper.hideView(inflate.llActivityEditYuyingshi4);
        }
        String text = "";
        if (TextUtils.equals(type, "1")) {
            text = "26";
        } else if (TextUtils.equals(type, "2")) {
            text = "30";
        } else if (TextUtils.equals(type, "3")) {
            text = "1";
        }
        myServerInfoVm.setDays(text);
        inflate.heavenNumberActivityAddMyService.setSubTitleRight(text);
    }


    InputConfirmPopupView inputConfirmPopupView;


    public void onAddServiceHeavenNumber(View view) {
        XPopup.Builder builder = new XPopup.Builder(view.getContext());
        inputConfirmPopupView = builder
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated() {
                        inputConfirmPopupView.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                    }
                })
                .asInputConfirm("天数", "请输入内容。",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                myServerInfoVm.setDays(text);
                                inflate.heavenNumberActivityAddMyService.setSubTitleRight(text);
                            }
                        });
        inputConfirmPopupView
                .show();
    }

    public void onAddServicePrice(View view) {
        XPopup.Builder builder = new XPopup.Builder(view.getContext());
        inputConfirmPopupView = builder
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated() {
                        inputConfirmPopupView.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                    }
                })
                .asInputConfirm("价格", "请输入内容。",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                myServerInfoVm.setTotalPrice(text);
                                inflate.priceActivityAddMyService.setSubTitleRight(text);
                            }
                        });
        inputConfirmPopupView
                .show();
    }

    public void onClickClear(View view) {
        myServerInfoVm = new MyServerInfoVm();
        loadData();
    }

    public void onClickSubmit(View view) {
        String msg = "";
        for (int i = 0; i < inflate.llActivityEditYuesao2.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.llActivityEditYuesao2.getChildAt(i);
            String tag = (String) childAt.getTag();
            if (childAt.isSelect()) {
                if (TextUtils.isEmpty(msg)) {
                    msg = tag;
                } else {
                    msg = msg + "," + tag;
                }
            }
        }
        myServerInfoVm.setSpecialCare(msg);

        presenter.requestSubmitService(myServerInfoVm);
    }
}
