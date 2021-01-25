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
import com.xiuxiu.confinement_nurse.databinding.ActivityLearningExperienceBinding;
import com.xiuxiu.confinement_nurse.help.DateHelper;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.my.adapter.SwitchAdapter;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 学习经历
 */
public class LearningExperienceActivity extends AbsBaseActivity implements LearningExperienceContract.IView {
    private static final int IMAGE_CODE = 1;
    private String id;
    public static void start(Context context) {
        start(context,"", null);
    }
    public static void start(Context context,LearningExperienceVm.LearningExperience learningExperience){
        start(context,"",learningExperience);
    }


    public static void start(Context context,String ysId) {
        start(context,ysId, null);
    }

    public static void start(Context context, String ysId,LearningExperienceVm.LearningExperience learningExperience) {
        Intent starter = new Intent(context, LearningExperienceActivity.class);
        if (learningExperience != null) {
            starter.putExtra("data", learningExperience);
        }
        starter.putExtra("id",ysId);
        context.startActivity(starter);
    }

    private FilterRadioVm subjectData;
    private LearningExperienceContract.IPresenter presenter;
    private ActivityLearningExperienceBinding inflate;
    private SwitchAdapter switchAdapter;
    private LearningExperienceVm.LearningExperience learningExperience;
    private String path;
    private boolean isEditMode;


    @Override
    protected View getLayoutView() {
        inflate = ActivityLearningExperienceBinding.inflate(LayoutInflater.from(this));
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        learningExperience = (LearningExperienceVm.LearningExperience) intent.getSerializableExtra("data");

        if (learningExperience == null) {
            isEditMode = true;
            saveLearningView();
            updateSubmitView();
        } else {
            updateView(learningExperience);
        }
        updateSubmitView();
    }

    private void loadData() {
        learningExperience = (LearningExperienceVm.LearningExperience) getIntent().getSerializableExtra("data");
        if (learningExperience == null) {
            isEditMode = true;
        } else {
            isEditMode = false;
        }
        presenter.requestLearningData();
    }

    private void setListener() {
        switchAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (!isEditMode) {
                return;
            }
            changeResumeViewState(position, switchAdapter, new XFunc1<FilterRadioVm>() {
                @Override
                public void call(FilterRadioVm filterRadioVm) {
                    inflate.rvActivityLearningExperienceSubject.setText(filterRadioVm.getTitle());
                    subjectData = filterRadioVm;
                }
            });
        });

    }

    private void initViewState() {

        switchAdapter = new SwitchAdapter();
        presenter = new LearningExperiencePresenter(this);
        presenter.setId(id);
        switchAdapter.addChildClickViewIds(R.id.item_switch_radio_sb);
        inflate.recyclerView.setAdapter(switchAdapter);
        switchAdapter.setRadio(true);
        inflate.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void initView() {
        id=getIntent().getStringExtra("id");
    }

    @Override
    public void onRequestLearningData(List<FilterRadioVm> list) {
        switchAdapter.setNewInstance(list);
        switchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestDataEnd() {
        if (learningExperience == null) {
            isEditMode = true;
            saveLearningView();
            updateSubmitView();
        } else {
            updateView(learningExperience);
        }
    }


    private void changeResumeViewState(int position, SwitchAdapter filterResumeAdapter, XFunc1<FilterRadioVm> xFunc1) {
        FilterRadioVm safe = CollectionUtil.getSafe(filterResumeAdapter.getData(), position, null);
        if (safe != null) {
            int state = safe.getState();
            clearItemState(filterResumeAdapter);
            int state1 = state == 1 ? 0 : 1;
            safe.setState(state1);
            filterResumeAdapter.notifyItemChanged(position);

            xFunc1.call(safe);

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

    public void onClickUpdateSchool(View view) {
        DialogHelper.showInputDialog(view.getContext(), "请输入学校名称", new XFunc1<String>() {
            @Override
            public void call(String o) {
                inflate.tvLearningExperenceSchool.setText(o);
            }
        });
    }

    public void onClickUpdateTime(View view) {
        DialogHelper.showCalendar(this, new XFunc1<Date>() {
            @Override
            public void call(Date date) {
                String text = DateHelper.stampToDate2(date);
                inflate.tvLearningExperenceTime.setTag(DateHelper.stampToDate(date));
                inflate.tvLearningExperenceTime.setText(text);
                updateSubmitView();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CODE && RESULT_OK == resultCode) {
            if (data == null) {
                return;
            }
            List<Uri> uris = Matisse.obtainResult(data);
            List<String> mPaths = Matisse.obtainPathResult(data);


            Uri safe = CollectionUtil.getSafe(uris, 0, null);
            String path = CollectionUtil.getSafe(mPaths, 0, null);
            GlideHelper.loadUriImage(safe, 0, inflate.ivActivityLearningExperienceImage);
            ViewHelper.hideView(inflate.llActivityLearningExperienceBg);
            this.path = path;
            updateSubmitView();
        }
    }


    public void onClickSubmit(View view) {
        if (!isEditMode) {
            return;
        }
        LearningExperienceVm.LearningExperience learningExperience = this.learningExperience;
        if (learningExperience == null) {
            learningExperience = new LearningExperienceVm.LearningExperience();
        }
        learningExperience.setType("2");
        learningExperience.setDegree(String.valueOf(subjectData.getCode()));
        learningExperience.setSchool(inflate.tvLearningExperenceSchool.getText().toString());
        learningExperience.setStartTime(String.valueOf(inflate.tvLearningExperenceTime.getTag()));
        learningExperience.setEndTime(String.valueOf(inflate.tvLearningExperenceTimeEnd.getTag()));
        presenter.requestSubmit(path, learningExperience);
    }

    public void onCLickDelete(View view) {
        if (learningExperience != null) {
            presenter.requestDelete(learningExperience.getId());
        }
    }

    public void onClickUpdateTimeEnd(View view) {
        if (!isEditMode) {
            return;
        }
        DialogHelper.showCalendar(this, new XFunc1<Date>() {
            @Override
            public void call(Date date) {
                String text = DateHelper.stampToDate2(date);
                inflate.tvLearningExperenceTimeEnd.setTag(DateHelper.stampToDate(date));
                inflate.tvLearningExperenceTimeEnd.setText(text);
                updateSubmitView();
            }
        });
    }

    public void onAdd(View view) {
        new XPopup.Builder(view.getContext())
                .atView(view)
                .asAttachList(new String[]{"添加学习经历", "添加教育经历", "编辑"},
                        new int[]{},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {
                                    ServiceExperienceActivity.start(view.getContext(), id,null);
                                } else if (position == 2) {
                                    isEditMode = true;
                                } else if (position == 1) {
                                    LearningExperienceActivity.start(view.getContext(),id);

                                }
                            }
                        })
                .show();
    }


    private void updateView(LearningExperienceVm.LearningExperience learningExperience) {
        if (learningExperience == null) {
            return;
        }
        isEditMode = false;
        clearItemState(switchAdapter);
        CollectionUtil._forEach(switchAdapter.getData(), new XFunc2<Integer, FilterRadioVm>() {
            @Override
            public void call(Integer integer, FilterRadioVm filterRadioVm) {
                if (TextUtils.equals(String.valueOf(filterRadioVm.getCode()), learningExperience.getSubject())) {
                    filterRadioVm.setState(1);
                    switchAdapter.notifyItemChanged(integer);
                }
            }
        });

            String s = DateHelper.stampToDate2(DateHelper.dateToStamp(learningExperience.getStartTime()));
            inflate.tvLearningExperenceTime.setText(s);

            String s2 = DateHelper.stampToDate2(DateHelper.dateToStamp(learningExperience.getEndTime()));
            inflate.tvLearningExperenceTimeEnd.setText(s2);
        inflate.tvLearningExperenceTime.setTag(learningExperience.getStartTime());

        inflate.tvLearningExperenceTimeEnd.setTag(learningExperience.getEndTime());
        inflate.tvLearningExperenceSchool.setText(learningExperience.getSchool());

        if (TextUtils.isEmpty(learningExperience.getImage())) {
            inflate.ivActivityLearningExperienceImage.setImageDrawable(null);
            inflate.ivActivityLearningExperienceImage.setBackground(null);
            ViewHelper.showView(inflate.llActivityLearningExperienceBg);
        } else {
            GlideHelper.loadImage(learningExperience.getImage(), inflate.ivActivityLearningExperienceImage);
            ViewHelper.hideView(inflate.llActivityLearningExperienceBg);
        }


        subjectData = new FilterRadioVm();
        subjectData.setState(1);
        subjectData.setTitle(UserHelper.serviceType.get(learningExperience.getSubject()));
        subjectData.setCode(Integer.parseInt(learningExperience.getSubject()));


    }
    private void updateSubmitView() {
        if (learningExperience == null) {
            ViewHelper.hideView(inflate.btActivityLearningExperienceDelete);
            inflate.btActivityLearningExperienceDelete.setEnabled(false);
        } else {
            ViewHelper.showView(inflate.btActivityLearningExperienceDelete);
            inflate.btActivityLearningExperienceDelete.setEnabled(true);
        }

        if (subjectData == null) {
            inflate.btActivityLearningExperienceDetermine.setEnabled(false);
            return;
        }

        if (TextUtils.isEmpty(inflate.tvLearningExperenceSchool.getText().toString())) {
            inflate.btActivityLearningExperienceDetermine.setEnabled(false);
            return;
        }
        if (TextUtils.isEmpty(inflate.tvLearningExperenceTime.getText().toString())) {
            inflate.btActivityLearningExperienceDetermine.setEnabled(false);
            return;
        }
        if (TextUtils.isEmpty(inflate.tvLearningExperenceTimeEnd.getText().toString())) {
            inflate.btActivityLearningExperienceDetermine.setEnabled(false);
            return;
        }

        if (learningExperience == null) {
            if (TextUtils.isEmpty(path)) {
                inflate.btActivityLearningExperienceDetermine.setEnabled(false);
                return;
            }
        } else {
            if (TextUtils.isEmpty(learningExperience.getImage())) {
                inflate.btActivityLearningExperienceDetermine.setEnabled(false);
                return;
            }
        }
        inflate.btActivityLearningExperienceDetermine.setEnabled(true);
    }
    private void saveLearningView() {
        clearItemState(switchAdapter);
        path = "";
        inflate.ivActivityLearningExperienceImage.setImageDrawable(null);
        inflate.ivActivityLearningExperienceImage.setBackground(null);
        ViewHelper.showView(inflate.llActivityLearningExperienceBg);
        inflate.tvLearningExperenceSchool.setText("");
        inflate.tvLearningExperenceTime.setText("");
        inflate.tvLearningExperenceTime.setTag("");
        inflate.tvLearningExperenceTimeEnd.setText("");
        inflate.tvLearningExperenceTimeEnd.setTag("");
        subjectData = null;
    }
}
