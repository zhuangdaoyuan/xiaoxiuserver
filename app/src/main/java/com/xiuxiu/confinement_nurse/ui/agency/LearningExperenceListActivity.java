package com.xiuxiu.confinement_nurse.ui.agency;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityLearningExperienceListBinding;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.my.LearningExperienceActivity;
import com.xiuxiu.confinement_nurse.ui.my.ServiceExperienceActivity;
import com.xiuxiu.confinement_nurse.ui.my.adapter.LearningExperienceAdapter;
import com.xiuxiu.confinement_nurse.ui.my.vm.LearningExperienceVm;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;

public class LearningExperenceListActivity extends AbsBaseActivity implements LearningExperenceListContract.IView {

    private  ActivityLearningExperienceListBinding inflate;
    private String matronId;
    private LearningExperenceListContract.IPresenter mPresenter;
    private LearningExperienceAdapter learningExperienceAdapter;
    public static void start(Context context,String id) {
        Intent starter = new Intent(context, LearningExperenceListActivity.class);
        starter.putExtra("id",id);
        context.startActivity(starter);
    }
    @Override
    protected View getLayoutView() {
        inflate = ActivityLearningExperienceListBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter=new LearningExperenceListPresenter(this);
        matronId=getIntent().getStringExtra("id");

        learningExperienceAdapter = new LearningExperienceAdapter();
        View view = LayoutInflater.from(this).inflate(R.layout.layout_learning_experience_header, null);
        learningExperienceAdapter.addHeaderView(view);
        inflate.rvActivityLearningExperienceList.setAdapter(learningExperienceAdapter);
        inflate.rvActivityLearningExperienceList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        learningExperienceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Object safe = CollectionUtil.getSafe(adapter.getData(), position, null);
                if (safe instanceof LearningExperienceVm.LearningExperience) {
                    String type = ((LearningExperienceVm.LearningExperience) safe).getType();
                    if (TextUtils.equals(type, "1")) {
                        ServiceExperienceActivity.start(view.getContext(), matronId,(LearningExperienceVm.LearningExperience) safe);
                    } else if (TextUtils.equals(type, "2")) {
                        LearningExperienceActivity.start(view.getContext(), matronId,
                                (LearningExperienceVm.LearningExperience) safe);
                    }
                }
            }
        });
        mPresenter.requestLoad(matronId);


        inflate.srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestLoad(matronId);
            }
        });

        EventBus.LearningExperience().observe(this, experienceEvent -> {
            mPresenter.requestLoad(matronId);
        });
    }


    @Override
    public void onRequestLearningExperience(LearningExperienceVm otherExperienceVm) {
        inflate.srl.setRefreshing(false);
        learningExperienceAdapter.setNewInstance(otherExperienceVm.getItems());
        learningExperienceAdapter.notifyDataSetChanged();
    }
    public void onClickAdd(View view){
        LearningExperienceActivity.start(view.getContext(),matronId);
    }
}
