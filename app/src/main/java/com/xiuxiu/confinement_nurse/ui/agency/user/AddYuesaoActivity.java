package com.xiuxiu.confinement_nurse.ui.agency.user;

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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.contrarywind.interfaces.IPickerViewData;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityAddYuesaoBinding;
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
import com.xiuxiu.confinement_nurse.model.event.LoginEvent;
import com.xiuxiu.confinement_nurse.model.event.UserInfoEvent;

import com.xiuxiu.confinement_nurse.model.event.YuesaoInfoEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.my.LocationActivity;
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
 * 添加或者修改月嫂信息，如果有id就是修改
 */
@Route(path = RouterHelper.Agency.YUESAOMY_INFO, extras = KEY_NEED_LOGIN)
public class AddYuesaoActivity extends AbsBaseActivity implements AddYuesaoContract.IView {
    private static final int IMAGE_CODE = 1;
    public static final int LOCATION_CODE = 2;

    public static void start() {
        ARouter.getInstance().build(RouterHelper.Agency.YUESAOMY_INFO).navigation();
    }

    public static void start(UserInfoBean userInfoBean) {
        ARouter.getInstance().build(RouterHelper.Agency.YUESAOMY_INFO)
                .withSerializable("data", userInfoBean)
                .navigation();
    }

    private ActivityAddYuesaoBinding inflate;
    private AddYuesaoContract.IPresenter presenter;
    private UserInfoContract.IPresenter userInfoPrsenter;

    @Override
    protected View getLayoutView() {
        inflate = ActivityAddYuesaoBinding.inflate(LayoutInflater.from(this));
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
        Intent intent = getIntent();
        UserInfoBean yuesaoInfoBean = (UserInfoBean) intent.getSerializableExtra("data");
        if (yuesaoInfoBean == null) {
            //添加
            onRequestPageSuccess();
        } else {
            loadUser(yuesaoInfoBean);
        }
    }

    private void setListener() {
        inflate.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestUpdateUserInfo();
            }
        });
        inflate.tvActivityMyInfoReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse
                        .from(AddYuesaoActivity.this)
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
        presenter = new AddYuesaoPresenter(this);
        userInfoPrsenter = new UserInfoPresenter(this);
    }

    private void initView() {
    }

    private void loadUser(UserInfoBean userBean) {
        presenter.setUserInfo(userBean);
        inflate.xfMyInfoName.setTitleRight1(TextUtils.isEmpty(userBean.getUserName()) ? "未填写" : userBean.getUserName());
        UserInfoBean item = userBean;
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
            String url = CollectionUtil.getSafe(mPaths, 0, "");
            Glide.with(inflate.image1).load(url)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .placeholder(R.color.color_backdrop_placeholder)
                    .error(R.color.color_backdrop_placeholder)
                    .into(inflate.image1);
        } else if (requestCode == LOCATION_CODE && RESULT_OK == resultCode) {
            if (data == null) {
                return;
            }
//            UserInfoBean userInfoBean = new UserInfoBean();
            String code = data.getStringExtra("code");
            presenter.setLocation(code);
            String name = data.getStringExtra("name");
            presenter.setAddress(name);

            inflate.xfMyInfoResidentialAddress.setTitleRight1(name);

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
                presenter.rquestName(title);
                inflate.xfMyInfoName.setTitleRight1(TextUtils.isEmpty(title) ? "未填写" : title);
            }
        });
    }

    public void onUpdateSix(View view) {
        new XPopup.Builder(view.getContext())
                .asBottomList("请选择一项", new String[]{"男", "女"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                presenter.requestSex(position == 0 ? 1 : 2);
                                inflate.xfMyInfoSex.setTitleRight1(position == 0 ? "男" : "女");
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
                presenter.requestBirthday(DateHelper.stampToDate(date));
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
        LocationActivity.start(AddYuesaoActivity.this);
    }

    public void onUpdateHometown(View view) {
        DialogHelper.showCityPickerPopup(view.getContext(), new XFunc3<IPickerViewData, IPickerViewData, IPickerViewData>() {
            @Override
            public void call(IPickerViewData iPickerViewData, IPickerViewData iPickerViewData2, IPickerViewData iPickerViewData3) {
//                UserInfoBean userInfoBean = new UserInfoBean();

                String nativePlace = iPickerViewData3 != null ? iPickerViewData3.getPickerViewCode() : iPickerViewData2 != null ? iPickerViewData2.getPickerViewCode() : iPickerViewData == null ? "-1" : iPickerViewData.getPickerViewCode();
                presenter.setNativePlace(nativePlace);
                String s1 = iPickerViewData != null ? iPickerViewData.getPickerViewText() : "";
                String s2 = iPickerViewData2 != null ? iPickerViewData2.getPickerViewText() : "";
                String s3 = iPickerViewData3 != null ? iPickerViewData3.getPickerViewText() : "";
                String nativePlaceName = s1 + "-" + s2 + "-" + s3;
                presenter.setNativePlaceName(nativePlaceName);

                String msg = iPickerViewData == null ? "" : iPickerViewData.getPickerViewText();
                String msg2 = iPickerViewData2 == null ? "" : iPickerViewData2.getPickerViewText();
                String msg3 = iPickerViewData3 == null ? "" : iPickerViewData3.getPickerViewText();
                String titleRight1 = msg + "-" + msg2 + "-" + msg3;
                inflate.xfMyInfoHometown.setTitleRight1(titleRight1);
            }
        });

    }

//    @Override
//    public void onRequestUploadFileSuccess(String mImageUrls, List<Uri> uris) {
//        loadFileImage(uris);
//    }

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
//                                UserInfoBean userInfoBean = new UserInfoBean();
                                presenter.setIdNo(idCard);
                                inflate.xfMyInfoIdCard.setTitleRight1(idCard);
                            }
                        });
        inputConfirmPopupView.show();
//        inputConfirmPopupView.post(new Runnable() {
//            @Override
//            public void run() {
//                EditText editText = inputConfirmPopupView.getEditText();
//                if (editText != null) {
//                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
//                }
//            }
//        });
    }

    public void onUpdateWork(View view) {
//        InputConfirmPopupView inputConfirmPopupView = new XPopup.Builder(this)
//                .asInputConfirm("工作年限。", "请输入内容",
//                        new OnInputConfirmListener() {
//                            @Override
//                            public void onConfirm(String title) {
//                                UserInfoBean userInfoBean = new UserInfoBean();
//                                userInfoBean.setWorkYears(title);
//                                presenter.requestUpdateUserInfo(userInfoBean, new XFunc0() {
//                                    @Override
//                                    public void call() {
//                                        inflate.xfMyInfoWork.setTitleRight1(title);
//                                        UserBean userBean = ModelManager.getInstance().getUserInterface().requestCurrentUserBean();
//                                        userBean.getItem().setWorkYears(title);
//                                        ModelManager.getInstance().getUserInterface().updateUserBean(userBean);
//                                    }
//                                });
//                            }
//                        });
//        inputConfirmPopupView.show();
//        inputConfirmPopupView.post(new Runnable() {
//            @Override
//            public void run() {
//                EditText editText = inputConfirmPopupView.getEditText();
//                if (editText != null) {
//                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
//                }
//            }
//        });
//

    }

    @Override
    public void onRequestOK() {
        EventBus.UpdateEvent().post(new YuesaoInfoEvent());
        finish();
    }
}
