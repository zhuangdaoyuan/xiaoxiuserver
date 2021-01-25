package com.xiuxiu.confinement_nurse.ui.my;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityOherExperienceBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.my.adapter.SwitchAdapter;
import com.xiuxiu.confinement_nurse.ui.my.vm.OtherExperienceVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.PathVm;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rxhttp.wrapper.utils.GsonUtil;

/**
 * 其他 经历
 */
public class OtherExperienceActivity extends AbsBaseActivity implements OtherExperienceContract.IView {
    private static final int IMAGE_CODE = 1;
    private static final int IMAGE_CODE_2 = 2;
    private static final int IMAGE_CODE_3 = 3;
    private OtherExperienceVm.OtherExperience otherExperience;

    public static void start(Context context,String ysId, OtherExperienceVm.OtherExperience otherExperience) {
        Intent starter = new Intent(context, OtherExperienceActivity.class);
        starter.putExtra("data", otherExperience);
        starter.putExtra("ysId",ysId);
        context.startActivity(starter);
    }

    private OtherExperienceContract.IPresenter presenter;
    private ActivityOherExperienceBinding inflate;
    private SwitchAdapter serviceAdapter;
    private SwitchAdapter typeAdapter;
    private FilterRadioVm subjectData;
    private FilterRadioVm levelData;
    private String organizationType;
    private String organizationName;
    private PathVm path=new PathVm();
    private PathVm path2=new PathVm();
    private PathVm path3=new PathVm();

    @Override
    protected View getLayoutView() {
        inflate = ActivityOherExperienceBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected Object getTargetView() {
        return inflate.csvActivityLearningBg;
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

        presenter.requestData();
    }

    private void setListener() {
        serviceAdapter.setOnItemChildClickListener((adapter, view, position) -> changeResumeViewState(position, serviceAdapter, new XFunc1<FilterRadioVm>() {
            @Override
            public void call(FilterRadioVm filterRadioVm) {
                subjectData = filterRadioVm;
                inflate.rvActivityOherExperienceSubject.setText(filterRadioVm.getTitle());
                updateSubmitView();
            }
        }));
        typeAdapter.setOnItemChildClickListener((adapter, view, position) -> changeResumeViewState(position, typeAdapter, new XFunc1<FilterRadioVm>() {
            @Override
            public void call(FilterRadioVm filterRadioVm) {
                levelData = filterRadioVm;
                inflate.tvactivityOherExperienceServiceSubject.setText(filterRadioVm.getTitle());
                updateSubmitView();
            }
        }));
    }

    private void initViewState() {
        String ysId = getIntent().getStringExtra("ysId");
        otherExperience = (OtherExperienceVm.OtherExperience) getIntent().getSerializableExtra("data");
        serviceAdapter = new SwitchAdapter();
        presenter = new OtherExperiencePresenter(this);
        presenter.setYsId(ysId);
        serviceAdapter.addChildClickViewIds(R.id.item_switch_radio_sb);
        inflate.recyclerView.setAdapter(serviceAdapter);
        serviceAdapter.setRadio(true);
        inflate.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));


        typeAdapter = new SwitchAdapter();
        typeAdapter.addChildClickViewIds(R.id.item_switch_radio_sb);
        inflate.recyclerView2.setAdapter(typeAdapter);
        typeAdapter.setRadio(true);
        typeAdapter.addChildClickViewIds(R.id.item_switch_radio_sb);
        inflate.recyclerView2.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void initView() {
    }


    private void changeResumeViewState(int position, SwitchAdapter filterResumeAdapter, XFunc1<FilterRadioVm> xFunc1) {
        FilterRadioVm safe = CollectionUtil.getSafe(filterResumeAdapter.getData(), position, null);
        if (safe != null) {
            int state = safe.getState();
            clearItemState(filterResumeAdapter);
            int state1 = state == 1 ? 0 : 1;
            safe.setState(state1);
            filterResumeAdapter.notifyItemChanged(position);

            if (xFunc1 != null) {
                xFunc1.call(safe);
            }

        }
    }

    private void clearItemState(SwitchAdapter filterResumeAdapter) {
        CollectionUtil._forEach(filterResumeAdapter.getData(), new XFunc2<Integer, FilterRadioVm>() {
            @Override
            public void call(Integer integer, FilterRadioVm filterRadioVm) {
                if (filterRadioVm.getState() == 1) {
                    filterRadioVm.setState(0);
                    filterResumeAdapter.notifyItemChanged(integer);
                }
            }
        });
    }


    public void onClickUpdate(View view) {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(1)
                .spanCount(4)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(IMAGE_CODE);
    }
    public void onClickUpdate2(View view) {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(1)
                .spanCount(4)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(IMAGE_CODE_2);
    }

    public void onClickUpdate3(View view) {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(1)
                .spanCount(4)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(IMAGE_CODE_3);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == IMAGE_CODE||requestCode == IMAGE_CODE_2||requestCode == IMAGE_CODE_3) && RESULT_OK == resultCode) {
            if (data == null) {
                return;
            }

            List<Uri> uris = Matisse.obtainResult(data);
            List<String> mPaths = Matisse.obtainPathResult(data);

            Uri safe = CollectionUtil.getSafe(uris, 0, null);
            String path = CollectionUtil.getSafe(mPaths, 0, null);

            updateSubmitView();
            if (requestCode == IMAGE_CODE) {
                this.path.path=path;
                GlideHelper.loadUriImage(safe, 0, inflate.ivActivityLearningExperienceImage);
                ViewHelper.hideView(inflate.llActivityLearningExperienceBg);
                return;
            }
            if (requestCode == IMAGE_CODE_2) {
                this.path2.path=path;
                GlideHelper.loadUriImage(safe, 0, inflate.ivActivityLearningExperienceImage2);
                ViewHelper.hideView(inflate.llActivityLearningExperienceBg2);
                return;
            }
            if (requestCode == IMAGE_CODE_3) {
                this.path3.path=path;
                GlideHelper.loadUriImage(safe, 0, inflate.ivActivityLearningExperienceImage3);
                ViewHelper.hideView(inflate.llActivityLearningExperienceBg3);
                return;
            }
        }
    }

    @Override
    public void onRequestService(List<FilterRadioVm> list) {
        serviceAdapter.setNewInstance(list);
        serviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestType(List<FilterRadioVm> list) {
        typeAdapter.setNewInstance(list);
        typeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRequestData() {
        if (otherExperience == null) {
            updateSubmitView();
        } else {
            updateView(otherExperience);
        }
    }

    @Override
    public void onRequestSubmitSuccess() {
    }

    public void onClickUpdateLocation(View view) {
        DialogHelper.showInputDialog(view.getContext(), "地点", new XFunc1<String>() {
            @Override
            public void call(String o) {
                inflate.tvLearningExperenceSchool.setText(o);
                updateSubmitView();
            }
        });
    }

    public void onClickUpdateType(View view) {
        String[] strings = {"个人", "机构"};
        new XPopup.Builder(view.getContext())
                .asBottomList("请选择一项", strings,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                inflate.tvActivityOtherExperenceMechanism.setText(text);

                                if (TextUtils.equals(strings[position],"机构")) {
                                    organizationType = String.valueOf(2);
                                    DialogHelper.showInputDialog(view.getContext(), "机构名称", new XFunc1<String>() {
                                        @Override
                                        public void call(String o) {
                                            organizationName = o;
                                            inflate.tvActivityOtherExperenceMechanism.setText(o);
                                            updateSubmitView();
                                        }
                                    });
                                } else {
                                    organizationType = String.valueOf(1);
                                    updateSubmitView();
                                }
                            }
                        })
                .show();
    }

    public void onClickUpdateEndTime(View view) {
        DialogHelper.showCalendar(this, new XFunc1<Date>() {
            @Override
            public void call(Date date) {
                String text = DateHelper.stampToDate2(date);
                inflate.tvLearningExperenceTimeEnd.setText(text);
                inflate.tvLearningExperenceTimeEnd.setTag( DateHelper.stampToDate(date));
                updateSubmitView();
            }
        });
    }

    public void onClickUpdateTime(View view) {
        DialogHelper.showCalendar(this, new XFunc1<Date>() {
            @Override
            public void call(Date date) {
                String text = DateHelper.stampToDate2(date);
                inflate.tvLearningExperenceTime.setText(text);
                inflate.tvLearningExperenceTime.setTag( DateHelper.stampToDate(date));
                updateSubmitView();
            }
        });
    }


    private void updateSubmitView() {
        if (otherExperience == null) {
            ViewHelper.hideView(inflate.btActivityOtherExperienceDelete);
            inflate.btActivityOtherExperienceDelete.setEnabled(false);
        } else {
            ViewHelper.showView(inflate.btActivityOtherExperienceDelete);
            inflate.btActivityOtherExperienceDelete.setEnabled(true);
        }

        if (otherExperience == null && subjectData == null) {
            inflate.btActivityOtherExperienceDetermine.setEnabled(false);
            return;
        }
        if (otherExperience == null && levelData == null) {
            inflate.btActivityOtherExperienceDetermine.setEnabled(false);
            return;
        }
        if (otherExperience == null && TextUtils.isEmpty(inflate.tvActivityOtherExperenceMechanism.getText().toString())) {
            inflate.btActivityOtherExperienceDetermine.setEnabled(false);
            return;
        }
        if (otherExperience == null && TextUtils.isEmpty(inflate.tvLearningExperenceTime.getText().toString())) {
            inflate.btActivityOtherExperienceDetermine.setEnabled(false);
            return;
        }
        if (otherExperience == null && TextUtils.isEmpty(inflate.tvLearningExperenceTimeEnd.getText().toString())) {
            inflate.btActivityOtherExperienceDetermine.setEnabled(false);
            return;
        }
        inflate.btActivityOtherExperienceDetermine.setEnabled(true);
    }


    private void updateView(OtherExperienceVm.OtherExperience learningExperience) {
        if (learningExperience == null) {
            return;
        }
        clearItemState(serviceAdapter);
        CollectionUtil._forEach(serviceAdapter.getData(), new XFunc2<Integer, FilterRadioVm>() {
            @Override
            public void call(Integer integer, FilterRadioVm filterRadioVm) {
                if (TextUtils.equals(String.valueOf(filterRadioVm.getCode()), learningExperience.getServiceType())) {
                    filterRadioVm.setState(1);
                    serviceAdapter.notifyItemChanged(integer);
                }
            }
        });

        clearItemState(typeAdapter);
        CollectionUtil._forEach(typeAdapter.getData(), new XFunc2<Integer, FilterRadioVm>() {
            @Override
            public void call(Integer integer, FilterRadioVm filterRadioVm) {
                if (TextUtils.equals(String.valueOf(filterRadioVm.getCode()), learningExperience.getObjectType())) {
                    filterRadioVm.setState(1);
                    typeAdapter.notifyItemChanged(integer);
                }
            }
        });

        inflate.tvLearningExperenceTime.setText(learningExperience.getServiceStartTime());
        inflate.tvLearningExperenceTimeEnd.setText(learningExperience.getServiceEndTime());
        inflate.tvLearningExperenceSchool.setText(learningExperience.getLocation());

        if (TextUtils.equals(learningExperience.getGroupType(), "2")) {
            inflate.tvActivityOtherExperenceMechanism.setText(learningExperience.getGroupName());
        } else if (TextUtils.equals(learningExperience.getGroupType(), "1")) {
            inflate.tvActivityOtherExperenceMechanism.setText("个人");
        } else {
            inflate.tvActivityOtherExperenceMechanism.setText("其他");
        }

        if (TextUtils.isEmpty(learningExperience.getImages())) {
            inflate.ivActivityLearningExperienceImage.setImageDrawable(null);
            inflate.ivActivityLearningExperienceImage.setBackground(null);
            ViewHelper.showView(inflate.llActivityLearningExperienceBg);
        } else {
            String images = learningExperience.getImages();
            List<String> imagesList=null;
            try {
              imagesList = GsonUtil.fromJson(images, List.class);
            }catch (Exception e){

            }
            if (imagesList!=null) {
                for (int i = 0; i < imagesList.size(); i++) {
                    String s = imagesList.get(i);
                    if (i==0) {
                        path.url=s;
                        GlideHelper.loadImage(s, inflate.ivActivityLearningExperienceImage);
                        ViewHelper.hideView(inflate.llActivityLearningExperienceBg);
                        continue;
                    }
                    if (i==1) {
                        path2.url=s;
                        GlideHelper.loadImage(s, inflate.ivActivityLearningExperienceImage2);
                        ViewHelper.hideView(inflate.llActivityLearningExperienceBg2);
                        continue;
                    }
                    if (i==2) {
                        path3.url=s;
                        GlideHelper.loadImage(s, inflate.ivActivityLearningExperienceImage3);
                        ViewHelper.hideView(inflate.llActivityLearningExperienceBg3);
                        continue;
                    }
                }
            }
        }
    }

    public void onClickSubmit(View view) {
        OtherExperienceVm.OtherExperience learningExperience = otherExperience;
        if (learningExperience == null) {
            learningExperience = new OtherExperienceVm.OtherExperience();
        }
        if (subjectData == null) {
            if (otherExperience == null) {
                return;
            }
        } else {
            learningExperience.setServiceType(String.valueOf(subjectData.getCode()));
        }
        if (levelData == null) {
            if (otherExperience == null) {
                return;
            }
        } else {
            learningExperience.setObjectType(String.valueOf(levelData.getCode()));
        }

        if (TextUtils.isEmpty(organizationType)) {
            if (otherExperience == null) {
                return;
            }
        } else {
            learningExperience.setGroupType(organizationType);
        }

        if (TextUtils.equals(learningExperience.getGroupType(), "2")) {
            if (TextUtils.isEmpty(organizationName)) {
                if (otherExperience == null) {
                    return;
                }
            } else {
                learningExperience.setGroupName(organizationName);
            }
        }

        String location = inflate.tvLearningExperenceSchool.getText().toString();
        if (TextUtils.isEmpty(location)) {
            if (otherExperience == null) {
                return;
            }
        } else {
            learningExperience.setLocation(location);
        }


        Object tag1 = inflate.tvLearningExperenceTime.getTag();
        String serviceStartTime = String.valueOf(tag1);
        if (tag1 == null || TextUtils.isEmpty(serviceStartTime)) {
            if (otherExperience == null) {
                return;
            }
        } else {
            learningExperience.setServiceStartTime(serviceStartTime);
        }

        Object tag = inflate.tvLearningExperenceTimeEnd.getTag();
        String serviceEndTime = String.valueOf(tag);
        if (tag == null || TextUtils.isEmpty(serviceEndTime)) {
            if (otherExperience == null) {
                return;
            }
        } else {
            learningExperience.setServiceEndTime(serviceEndTime);
        }

        if (otherExperience == null) {
            presenter.requestSubmit(new ArrayList<PathVm>(){{
                add(path);
                add(path2);
                add(path3);
            }}, learningExperience);
        } else {
            presenter.requestSubmit(new ArrayList<PathVm>(){{
                add(path);
                add(path2);
                add(path3);
            }}, otherExperience.getId(), learningExperience);
        }
    }

    public void onCLickDelete(View view) {
        if (otherExperience != null) {
            presenter.requestDelete(otherExperience.getId());
        }
    }


}
