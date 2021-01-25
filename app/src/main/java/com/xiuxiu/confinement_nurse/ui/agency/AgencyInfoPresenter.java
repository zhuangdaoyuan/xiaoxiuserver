package com.xiuxiu.confinement_nurse.ui.agency;

import android.net.Uri;

import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.db.pojo.AgencyInfoBean;
import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import rxhttp.wrapper.param.RxHttp;

public class AgencyInfoPresenter extends BasePresenter<AgencyInfoContract.IView> implements  AgencyInfoContract.IPresenter {
    
    public AgencyInfoPresenter(AgencyInfoContract.IView viewer) {
        super(viewer);
    }
    private String headPortrait;
    private String mCurrentSelectImage1;
    private String mCurrentSelectImage2;
    private AgencyInfoBean agencyInfoBean=new AgencyInfoBean();

    public void requestUploadFile(List<String> mPaths, List<Uri> uris) {
        headPortrait = CollectionUtil.getSafe(mPaths, 0, "");
    }

    public void rquestName(String title) {
        agencyInfoBean.setTitle(title);
    }

    public void onUpdateContactPerson(String title) {
        agencyInfoBean.setContact(title);
    }

    public void onUpdatContactNumber(String title) {
        agencyInfoBean.setMobile(title);
    }

    public void onUpdateSelfIntroduction(String title) {
        agencyInfoBean.setDes(title);
    }

    public void requestUploadFile2(List<String> mPaths, List<Uri> uris) {
        mCurrentSelectImage1 = CollectionUtil.getSafe(mPaths, 0, "");
        mCurrentSelectImage2 = CollectionUtil.getSafe(mPaths, 1, "");
    }

    public void requestSub() {
//        Observable<String> uploadUrl = createUploadUrl(headPortrait);
//        Observable<String> uploadUrl1 = createUploadUrl(mCurrentSelectImage1);
//        Observable<String> uploadUrl2 = createUploadUrl(mCurrentSelectImage2);
//        Observable.zip(uploadUrl, uploadUrl1, uploadUrl2, new Function3<String, String, String, AgencyInfoBean>() {
//            @NonNull
//            @Override
//            public AgencyInfoBean apply(@NonNull String s, @NonNull String s2, @NonNull String s3) throws Exception {
//                agencyInfoBean.setOrgImg(s);
//                agencyInfoBean.setForeImg(s2);
//                agencyInfoBean.setTailImg(s3);
//                return agencyInfoBean;
//            }
//        })
//                .flatMap(new Function<AgencyInfoBean, ObservableSource<AgencyInfoBean>>() {
//                    @Override
//                    public ObservableSource<AgencyInfoBean> apply(@NonNull AgencyInfoBean agencyInfoBean) throws Exception {
//                        RxHttp.postForm(Configuration.mechanism.KEY_JG_REGISTER)
//                                .add("title", name)
//                                .add("orgMobile", title1)
//                                .add("contact", person)
//                                .add("foreImg", strings.get(0))
//                                .add("tailImg", strings.get(1))
//                                .add("phone", phone)
//                                .add("userType", "4")
//                                .asXXResponse(LoginBean.class);
//                        return null;
//                    }
//                })
    }

    public void setData(AgencyInfoBean agencyInfoBean) {
        this.agencyInfoBean=agencyInfoBean;
    }
}
