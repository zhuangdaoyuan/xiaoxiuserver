package com.xiuxiu.confinement_nurse.ui.my;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.contrarywind.interfaces.IPickerViewData;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.databinding.ActivityMyInfoBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.help.ToastHelper;
import com.xiuxiu.confinement_nurse.help.UtilsHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserInfoBean;
import com.xiuxiu.confinement_nurse.model.event.UserInfoEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.user.UserInfoContract;
import com.xiuxiu.confinement_nurse.ui.user.UserInfoPresenter;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc3;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.Date;
import java.util.List;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_NEED_LOGIN;

/**
 * 基本信息
 */
@Route(path = RouterHelper.KEY_MY_INFO, extras = KEY_NEED_LOGIN)
public class MyInfoActivity extends AbsBaseActivity implements MyInfoContract.IView {
    private static final int IMAGE_CODE = 1;
    public static final int LOCATION_CODE = 2;

    public static void start() {
        ARouter.getInstance().build(RouterHelper.KEY_MY_INFO).navigation();
    }
    public static void start(String ysId) {
        ARouter.getInstance().build(RouterHelper.KEY_MY_INFO)
                .withString("ysid",ysId)
                .navigation();
    }

    private ActivityMyInfoBinding inflate;
    private MyInfoContract.IPresenter presenter;
    private UserInfoContract.IPresenter userInfoPrsenter;
    @Override
    protected View getLayoutView() {
        inflate = ActivityMyInfoBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected Object getTargetView() {
        return inflate.svActivityMyInfoContent;
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
        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
        loadUser(userBean);
        userInfoPrsenter.requestUserInfo(userBean.getToken(), userBean.getId(), new XFunc0() {
            @Override
            public void call() {
                UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                loadUser(userBean);
            }
        }, new XFunc0() {
            @Override
            public void call() {

            }
        });
    }

    private void setListener() {
        inflate.tvActivityMyInfoReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse
                        .from(MyInfoActivity.this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .countable(true)
                        .maxSelectable(1)
//                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .spanCount(4)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(IMAGE_CODE);
            }
        });
    }

    private void initViewState() {
        presenter = new MyInfoPresenter(this);
        userInfoPrsenter = new UserInfoPresenter(this);

        String ysid = getIntent().getStringExtra("ysid");
        presenter.setYsid(ysid);
    }

    private void initView() {
    }

    private void loadUser(UserBean userBean) {
        inflate.xfMyInfoName.setTitleRight1(TextUtils.isEmpty(userBean.getItem().getUserName()) ? "未填写" : userBean.getItem().getUserName());
        UserInfoBean item = userBean.getItem();
        if (item == null) {
            return;
        }
        inflate.xfMyInfoSex.setTitleRight1(item.getSex() == 1 ? "男" : "女");
        inflate.xfMyInfoAge.setTitleRight1(item.getBirthday());
        inflate.xfMyInfoResidentialAddress.setTitleRight1(item.getAddress());
        inflate.xfMyInfoHometown.setTitleRight1(item.getNativePlaceName());
        inflate.xfMyInfoIdCard.setTitleRight1(item.getIdNo());
        inflate.xfMyInfoWork.setTitleRight1(item.getWorkYears());


        String avatarList = item.getAvatarList();
        if (!TextUtils.isEmpty(avatarList)) {
            String[] split = avatarList.split(",");
            loaUriImage(split[0]);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CODE && RESULT_OK == resultCode) {
            if (data == null) {
                return;
            }
            List<Uri> uris = Matisse.obtainResult(data);
            List<String> mPaths = Matisse.obtainPathResult(data);
            presenter.requestUploadFile(mPaths, uris);
        }else if(requestCode==LOCATION_CODE&& RESULT_OK == resultCode){
            if (data == null) {
                return;
            }
            UserInfoBean userInfoBean = new UserInfoBean();
            String code = data.getStringExtra("code");
            userInfoBean.setLocation(code);
            String name = data.getStringExtra("name");
            userInfoBean.setAddress(name);
                presenter.requestUpdateUserInfo(userInfoBean, new XFunc0() {
                    @Override
                    public void call() {
                        inflate.xfMyInfoResidentialAddress.setTitleRight1(name);

                        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                        userBean.getItem().setAddress(name);
                        userBean.getItem().setLocation(code);
                        ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
                    }
                });
        }
    }


    private void loaUriImage(String avatarList) {
        GlideHelper.loadImage(avatarList, inflate.image1);
    }

    private void loadFileImage(List<Uri> avatarList) {
        Uri safe = CollectionUtil.getSafe(avatarList, 0, null);
        GlideHelper.loadUriImage(safe, inflate.image1);
    }

    public void onUpdateName(View view) {
        DialogHelper.showInputDialog(this, "修改名称", new XFunc1<String>() {
            @Override
            public void call(String title) {
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setUserName(title);
                presenter.requestUpdateUserInfo(userInfoBean, new XFunc0() {
                    @Override
                    public void call() {
                        inflate.xfMyInfoName.setTitleRight1(TextUtils.isEmpty(title) ? "未填写" : title);

                        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                        userBean.getItem().setUserName(title);
                        ModelManager.getInstance().getUserInterface().updateUserBean(userBean);

                        EventBus.UpdateUserInfoByName().post(new UserInfoEvent());
                    }
                });
            }
        });
    }

    public void onUpdateSix(View view) {
        new XPopup.Builder(view.getContext())
                .asBottomList("请选择一项", new String[]{"男", "女"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                UserInfoBean userInfoBean = new UserInfoBean();
                                userInfoBean.setSex(position == 0 ? 1 : 2);
                                presenter.requestUpdateUserInfo(userInfoBean, new XFunc0() {
                                    @Override
                                    public void call() {
                                        inflate.xfMyInfoSex.setTitleRight1(position == 0 ? "男" : "女");

                                        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                                        userBean.getItem().setSex(position == 0 ? 1 : 2);
                                        ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
                                    }
                                });
                            }
                        })
                .show();
    }

    public void onUpdateAge(View view) {
        DialogHelper.showCalendar(this, new XFunc1<Date>() {
            @Override
            public void call(Date date) {

                String text = DateHelper.stampToDate2(date);
                inflate.xfMyInfoAge.setTitleRight1(text);
                UserInfoBean userInfoBean = new UserInfoBean();
                try {
                    userInfoBean.setBirthday(DateHelper.stampToDate(date));
                    presenter.requestUpdateUserInfo(userInfoBean, new XFunc0() {
                        @Override
                        public void call() {
                            UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                            userBean.getItem().setBirthday(text);
                            ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
                        }
                    });
                } catch (Exception e) {
                    ToastHelper.showToast("输入错误请重新输入");
                }
            }
        });


    }

    public void onUpdateAddress(View view) {
//        DialogHelper.showInputDialog(this, "修改地址", new XFunc1<String>() {
//            @Override
//            public void call(String title) {
//                UserInfoBean userInfoBean = new UserInfoBean();
//                userInfoBean.setAddress(title);
//                presenter.requestUpdateUserInfo(userInfoBean, new XFunc0() {
//                    @Override
//                    public void call() {
//                        inflate.xfMyInfoResidentialAddress.setTitleRight1(title);
//
//                        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
//                        userBean.getItem().setAddress(title);
//                        ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
//                    }
//                });
//            }
//        });
        LocationActivity.start(MyInfoActivity.this);
    }

    public void onUpdateHometown(View view) {
        DialogHelper.showCityPickerPopup(view.getContext(), new XFunc3<IPickerViewData, IPickerViewData, IPickerViewData>() {
            @Override
            public void call(IPickerViewData iPickerViewData, IPickerViewData iPickerViewData2, IPickerViewData iPickerViewData3) {
                UserInfoBean userInfoBean = new UserInfoBean();

                String nativePlace = iPickerViewData3 != null ? iPickerViewData3.getPickerViewCode() : iPickerViewData2 != null ? iPickerViewData2.getPickerViewCode() : iPickerViewData == null ? "-1" : iPickerViewData.getPickerViewCode();
                userInfoBean.setNativePlace(nativePlace);
                String s1 = iPickerViewData != null ? iPickerViewData.getPickerViewText() : "";
                String s2 = iPickerViewData2 != null ? iPickerViewData2.getPickerViewText() : "";
                String s3 = iPickerViewData3 != null ? iPickerViewData3.getPickerViewText() : "";
                String nativePlaceName = s1 + "-" + s2 +"-"+ s3;
                userInfoBean.setNativePlaceName(nativePlaceName);

                presenter.requestUpdateUserInfo(userInfoBean, new XFunc0() {
                    @Override
                    public void call() {
                        String msg = iPickerViewData == null ? "" : iPickerViewData.getPickerViewText();
                        String msg2 = iPickerViewData2 == null ? "" : iPickerViewData2.getPickerViewText();
                        String msg3 = iPickerViewData3 == null ? "" : iPickerViewData3.getPickerViewText();
                        String titleRight1 = msg + "-" + msg2 +"-"+msg3;
                        inflate.xfMyInfoHometown.setTitleRight1(titleRight1);

                        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                        userBean.getItem().setNativePlaceName(titleRight1);
                        userBean.getItem().setNativePlace(nativePlace);

                        ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
                    }
                });
            }
        });

    }

    @Override
    public void onRequestUploadFileSuccess(String mImageUrls, List<Uri> uris) {
        loadFileImage(uris);
    }

    public void onUpdateIdCard(View view) {
        InputConfirmPopupView inputConfirmPopupView = new XPopup.Builder(this)
                .asInputConfirm("身份证号码。", "请输入内容",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String idCard) {
                                if (TextUtils.isEmpty(idCard)) {
                                    ToastHelper.showToast("身份证号不能为空");
                                    return;
                                }
                                if (!UtilsHelper.isIDCard(idCard)) {
                                    ToastHelper.showToast("请输入正常的身份证号");
                                    return;
                                }
                                UserInfoBean userInfoBean = new UserInfoBean();
                                userInfoBean.setIdNo(idCard);
                                presenter.requestUpdateUserInfo(userInfoBean, new XFunc0() {
                                    @Override
                                    public void call() {
                                        inflate.xfMyInfoIdCard.setTitleRight1(idCard);
                                        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                                        userBean.getItem().setIdNo(idCard);
                                        ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
                                    }
                                });
                            }
                        });
        inputConfirmPopupView.show();
        inputConfirmPopupView.post(new Runnable() {
            @Override
            public void run() {
                EditText editText = inputConfirmPopupView.getEditText();
                if (editText != null) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                }
            }
        });
    }

    public void onUpdateWork(View view) {
        InputConfirmPopupView inputConfirmPopupView = new XPopup.Builder(this)
                .asInputConfirm("工作年限。", "请输入内容",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String title) {
                                UserInfoBean userInfoBean = new UserInfoBean();
                                userInfoBean.setWorkYears(title);
                                presenter.requestUpdateUserInfo(userInfoBean, new XFunc0() {
                                    @Override
                                    public void call() {
                                        inflate.xfMyInfoWork.setTitleRight1(title);
                                        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
                                        userBean.getItem().setWorkYears(title);
                                        ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
                                    }
                                });
                            }
                        });
        inputConfirmPopupView.show();
        inputConfirmPopupView.post(new Runnable() {
            @Override
            public void run() {
                EditText editText = inputConfirmPopupView.getEditText();
                if (editText != null) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                }
            }
        });


    }
}
