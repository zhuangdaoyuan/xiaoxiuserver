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
import com.xiuxiu.confinement_nurse.databinding.ActivityServiceExperienceBinding;
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
 *
 */
public class ServiceExperienceActivity extends AbsBaseActivity implements ServiceExperienceContract.IView {
    private static final int IMAGE_CODE = 1;

    private LearningExperienceVm.LearningExperience learningExperience;

    private String ysId;
    public static void start(Context context, LearningExperienceVm.LearningExperience learningExperience) {
      start(context,"",learningExperience);
    }
    public static void start(Context context, String ysId,LearningExperienceVm.LearningExperience learningExperience) {
        Intent starter = new Intent(context, ServiceExperienceActivity.class);
        if (learningExperience != null) {
            starter.putExtra("data", learningExperience);
        }
        if (!TextUtils.isEmpty(ysId)) {
            starter.putExtra("id",ysId);
        }
        context.startActivity(starter);
    }

    private ServiceExperienceContract.IPresenter presenter;
    private ActivityServiceExperienceBinding inflate;
    private SwitchAdapter serviceAdapter;
    private SwitchAdapter typeAdapter;

    private FilterRadioVm subjectData;
    private FilterRadioVm levelData;

    private String path;
    private boolean isEdit;

    @Override
    protected View getLayoutView() {
        inflate = ActivityServiceExperienceBinding.inflate(LayoutInflater.from(this));
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
        learningExperience = (LearningExperienceVm.LearningExperience) getIntent().getSerializableExtra("data");
        initViewState();
        setListener();
        loadData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        learningExperience = (LearningExperienceVm.LearningExperience) intent.getSerializableExtra("data");
        ysId=intent.getStringExtra("id");
        if (learningExperience == null) {
            isEdit = true;
            saveLearningView();
            updateSubmitView();
        } else {
            updateView(learningExperience);
        }
        updateSubmitView();
    }


    private void loadData() {
        presenter.setYsId(ysId);
        presenter.requestServiceData();

    }

    private void setListener() {
        serviceAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (!isEdit) {
                return;
            }
            changeResumeViewState(position, serviceAdapter, new XFunc1<FilterRadioVm>() {
                @Override
                public void call(FilterRadioVm filterRadioVm) {
                    inflate.tvActivityServiceExperienceSubject.setText(filterRadioVm.getTitle());
                    subjectData = filterRadioVm;
                    updateSubmitView();
                }
            });
        });
        typeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (!isEdit) {
                return;
            }
            changeResumeViewState(position, typeAdapter, new XFunc1<FilterRadioVm>() {
                @Override
                public void call(FilterRadioVm filterRadioVm) {
                    inflate.tvActivityServiceExperienceLevel.setText(filterRadioVm.getTitle());
                    levelData = filterRadioVm;
                    updateSubmitView();
                }
            });
        });
    }

    private void initViewState() {
        serviceAdapter = new SwitchAdapter();
        presenter = new ServiceExperiencePresenter(this);

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
        ysId=getIntent().getStringExtra("id");
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

    public void onClickUpdateTimeEnd(View view) {
        if (!isEdit) {
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

    public void onClickUpdateTime(View view) {
        if (!isEdit) {
            return;
        }
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
        if (!isEdit) {
            return;
        }
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

    @Override
    public void onRequestSubjectData(List<FilterRadioVm> list) {
        serviceAdapter.setNewInstance(list);
        serviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestType(List<FilterRadioVm> list) {
        typeAdapter.setNewInstance(list);
        typeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRequestDataEnd() {
        if (learningExperience == null) {
            isEdit = true;
            saveLearningView();
            updateSubmitView();
        } else {
            updateView(learningExperience);
        }
    }


    public void onClickUpdateAgency(View view) {
        if (!isEdit) {
            return;
        }
        DialogHelper.showInputDialog(view.getContext(), "培训机构", new XFunc1<String>() {
            @Override
            public void call(String o) {
                inflate.tvActivityServiceExperienceAgency.setText(o);
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
                                    ServiceExperienceActivity.start(view.getContext(), ysId,null);
                                } else if (position == 2) {
                                    isEdit = true;
                                } else if (position == 1) {
                                    LearningExperienceActivity.start(view.getContext(),ysId);
                                }
                            }
                        })
                .show();
    }


    private void saveLearningView() {
        clearItemState(serviceAdapter);
        clearItemState(typeAdapter);
        path = "";
        inflate.ivActivityLearningExperienceImage.setImageDrawable(null);
        inflate.ivActivityLearningExperienceImage.setBackground(null);
        ViewHelper.showView(inflate.llActivityLearningExperienceBg);
        inflate.tvActivityServiceExperienceAgency.setText("");
        inflate.tvLearningExperenceTime.setText("");
        inflate.tvLearningExperenceTime.setTag("");
        inflate.tvLearningExperenceTimeEnd.setText("");
        inflate.tvLearningExperenceTimeEnd.setTag("");
        subjectData = null;
        levelData = null;
    }

    /**
     * 根据数据刷新view
     *
     * @param learningExperience
     */
    private void updateView(LearningExperienceVm.LearningExperience learningExperience) {

        if (learningExperience == null) {
            return;
        }
        isEdit = false;
        clearItemState(serviceAdapter);
        CollectionUtil._forEach(serviceAdapter.getData(), new XFunc2<Integer, FilterRadioVm>() {
            @Override
            public void call(Integer integer, FilterRadioVm filterRadioVm) {
                if (TextUtils.equals(String.valueOf(filterRadioVm.getCode()), learningExperience.getSubject())) {
                    filterRadioVm.setState(1);
                    serviceAdapter.notifyItemChanged(integer);
                }
            }
        });

        clearItemState(typeAdapter);
        CollectionUtil._forEach(typeAdapter.getData(), new XFunc2<Integer, FilterRadioVm>() {
            @Override
            public void call(Integer integer, FilterRadioVm filterRadioVm) {
                if (TextUtils.equals(String.valueOf(filterRadioVm.getCode()), learningExperience.getLevel())) {
                    filterRadioVm.setState(1);
                    typeAdapter.notifyItemChanged(integer);
                }
            }
        });


        String s = DateHelper.stampToDate2(DateHelper.dateToStamp(learningExperience.getStartTime()));
        inflate.tvLearningExperenceTime.setText(s);

        String s2 = DateHelper.stampToDate2(DateHelper.dateToStamp(learningExperience.getEndTime()));
        inflate.tvLearningExperenceTimeEnd.setText(s2);


        inflate.tvLearningExperenceTime.setTag(learningExperience.getStartTime());

        inflate.tvLearningExperenceTimeEnd.setTag(learningExperience.getEndTime());
        inflate.tvActivityServiceExperienceAgency.setText(learningExperience.getSchool());

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


        levelData = new FilterRadioVm();
        levelData.setState(1);
        levelData.setTitle(UserHelper.serviceType.get(learningExperience.getLevel()));
        levelData.setCode(Integer.parseInt(learningExperience.getLevel()));
    }

    private void updateSubmitView() {
        if (learningExperience == null) {
            ViewHelper.hideView(inflate.btDelete);
            inflate.btDelete.setEnabled(false);
        } else {
            ViewHelper.showView(inflate.btDelete);
            inflate.btDelete.setEnabled(true);
        }

        if (subjectData == null) {
            inflate.btActivityServiceExperienceDefine.setEnabled(false);
            return;
        }
        if (levelData == null) {
            inflate.btActivityServiceExperienceDefine.setEnabled(false);
            return;
        }
        if (TextUtils.isEmpty(inflate.tvActivityServiceExperienceAgency.getText().toString())) {
            inflate.btActivityServiceExperienceDefine.setEnabled(false);
            return;
        }
        if (TextUtils.isEmpty(inflate.tvLearningExperenceTime.getText().toString())) {
            inflate.btActivityServiceExperienceDefine.setEnabled(false);
            return;
        }
        if (TextUtils.isEmpty(inflate.tvLearningExperenceTimeEnd.getText().toString())) {
            inflate.btActivityServiceExperienceDefine.setEnabled(false);
            return;
        }

        if (learningExperience == null) {
            if (TextUtils.isEmpty(path)) {
                inflate.btActivityServiceExperienceDefine.setEnabled(false);
                return;
            }
        } else {
            if (TextUtils.isEmpty(learningExperience.getImage())) {
                inflate.btActivityServiceExperienceDefine.setEnabled(false);
                return;
            }
        }
        inflate.btActivityServiceExperienceDefine.setEnabled(true);
    }

    public void onCLickDelete(View view) {
        if (learningExperience != null) {
            presenter.requestDelete(learningExperience.getId());
        }
    }

    public void onClickSubmit(View view) {
        if (!isEdit) {
            return;
        }
        LearningExperienceVm.LearningExperience learningExperience = this.learningExperience;
        if (learningExperience == null) {
            learningExperience = new LearningExperienceVm.LearningExperience();
        }
        learningExperience.setType("1");
        learningExperience.setSubject(String.valueOf(subjectData.getCode()));
        learningExperience.setLevel(String.valueOf(levelData.getCode()));
        learningExperience.setSchool(inflate.tvActivityServiceExperienceAgency.getText().toString());
        learningExperience.setStartTime(String.valueOf(inflate.tvLearningExperenceTime.getTag()));
        learningExperience.setEndTime(String.valueOf(inflate.tvLearningExperenceTimeEnd.getTag()));
        presenter.requestSubmit(path, learningExperience);

    }
}
