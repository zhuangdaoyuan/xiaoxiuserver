package com.xiuxiu.confinement_nurse.ui.service;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityAddMyServerBinding;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.model.event.UserInfoEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;
import com.xiuxiu.confinement_nurse.ui.service.vm.MyServerInfoVm;
import com.xiuxiu.confinement_nurse.ui.view.SwitchButton;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.List;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_NEED_LOGIN;

@Route(path = RouterHelper.KEY_ADD_SERVICE, extras = KEY_NEED_LOGIN)
public class AddMyServiceActivity extends AbsBaseActivity implements MyServiceContract.IView {

    public static void start(Context context) {
        ARouter.getInstance().build(RouterHelper.KEY_ADD_SERVICE).navigation();
    }

    private ActivityAddMyServerBinding inflate;

    private int myServiceType;
    private String heavenNumber;
    private String price;
    private MyServiceContract.IPresenter presenter;

    @Override
    protected View getLayoutView() {
        inflate = ActivityAddMyServerBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRequestPageSuccess();
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void loadData() {
    }

    private void setListener() {
        inflate.otherActivityAddMyService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean select = inflate.otherActivityAddMyService1.isSelect();
                inflate.otherActivityAddMyService1.setButtonSelectState(!select);
                inflate.otherActivityAddMyService2.setButtonSelectState(false);
                inflate.otherActivityAddMyService3.setButtonSelectState(false);
            }
        });
        inflate.otherActivityAddMyService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean select = inflate.otherActivityAddMyService2.isSelect();
                inflate.otherActivityAddMyService2.setButtonSelectState(!select);
                inflate.otherActivityAddMyService1.setButtonSelectState(false);
                inflate.otherActivityAddMyService3.setButtonSelectState(false);
            }
        });
        inflate.otherActivityAddMyService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean select = inflate.otherActivityAddMyService3.isSelect();
                inflate.otherActivityAddMyService3.setButtonSelectState(!select);
                inflate.otherActivityAddMyService2.setButtonSelectState(false);
                inflate.otherActivityAddMyService1.setButtonSelectState(false);
            }
        });
    }

    private void initViewState() {
        presenter = new MyServicePresenter(this);
    }

    private void initView() {
    }

    public void onAddServiceType(View view) {
        new XPopup.Builder(view.getContext())
                .atView(inflate.serviceActivityAddMyService.findViewById(R.id.text3))
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onDismiss() {
                        inflate.serviceActivityAddMyService.setOpenState(false);
                    }
                })
                .asAttachList(new String[]{ "月嫂服务", "育婴师服务", "住院陪护", "母乳调理"},
                        new int[]{},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                myServiceType = position+1;
                                inflate.serviceActivityAddMyService.setSubTitleRight(text);
                            }
                        })
                .show();
    }
    InputConfirmPopupView inputConfirmPopupView;
    public void onAddServiceHeavenNumber(View view) {
        XPopup.Builder builder = new XPopup.Builder(this);
        inputConfirmPopupView = builder
                .setPopupCallback(new SimpleCallback(){
                    @Override
                    public void onCreated() {
                        inputConfirmPopupView.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                    }
                })
                .asInputConfirm("天数", "请输入内容。",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                heavenNumber = text;
                                inflate.heavenNumberActivityAddMyService.setSubTitleRight(text);
                            }
                        });
        inputConfirmPopupView
                .show();
    }

    public void onAddServicePrice(View view) {
        XPopup.Builder builder = new XPopup.Builder(this);
        inputConfirmPopupView = builder
                .setPopupCallback(new SimpleCallback(){
                    @Override
                    public void onCreated() {
                        inputConfirmPopupView.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                    }
                })
                .asInputConfirm("价格", "请输入内容。",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                price = text;
                                inflate.priceActivityAddMyService.setSubTitleRight(text);
                            }
                        });
        inputConfirmPopupView
                .show();
    }

    public void onAddService(View view) {
        MyServerInfoVm myServerInfoVm = new MyServerInfoVm();
        myServerInfoVm.setServiceType(String.valueOf(myServiceType));

        for (int i = 0; i < inflate.specialActivityAddMyService.getChildCount(); i++) {
            SwitchButton childAt = (SwitchButton) inflate.specialActivityAddMyService.getChildAt(i);
            if (childAt.isSelect()) {
                myServerInfoVm.setSpecialCare(String.valueOf(childAt.getTag()));
                break;
            }
        }
        myServerInfoVm.setDays(heavenNumber);
        myServerInfoVm.setTotalPrice(price);

        presenter.requestSubmitService(myServerInfoVm);
    }

    @Override
    public void onRequestMyServiceList(List<MyServerInfoVm> s) {

    }

    @Override
    public void onRequestDeleteSuccess(int position) {

    }
}
