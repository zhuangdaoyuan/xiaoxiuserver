package com.xiuxiu.confinement_nurse.ui.my;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivitySelfEvaluationBinding;
import com.xiuxiu.confinement_nurse.databinding.ActivityServiceExperienceBinding;
import com.xiuxiu.confinement_nurse.help.GlideHelper;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.ViewHelper;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.my.vm.SelfExperienceVm;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;


/**
 * 自我展示
 */
public class SelfEvaluationActivity extends AbsBaseActivity implements SelfEvaluationContract.IView {
    public static void start(Context context,String ysId, SelfExperienceVm selfExperienceVm) {
        Intent starter = new Intent(context, SelfEvaluationActivity.class);
        starter.putExtra("data", selfExperienceVm);
        starter.putExtra("ysId",ysId);
        context.startActivity(starter);
    }

    private static final int IMAGE_CODE = 1;

    private ActivitySelfEvaluationBinding inflate;
    private SelfExperienceVm selfExperienceVm;
    private SelfEvaluationContract.IPresenter presenter;
    private String path;

    @Override
    protected View getLayoutView() {
        inflate = ActivitySelfEvaluationBinding.inflate(LayoutInflater.from(this));
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
    }

    private void initViewState() {
        presenter = new SelfEvaluationPresenter(this);
        String ysId = getIntent().getStringExtra("ysId");
        presenter.setYsId(ysId);
        selfExperienceVm = (SelfExperienceVm) getIntent().getSerializableExtra("data");
        if (selfExperienceVm == null || (TextUtils.isEmpty(selfExperienceVm.getItem().getContent()) && TextUtils.isEmpty(selfExperienceVm.getItem().getVideo()))) {
            ViewHelper.showView(inflate.flActivitySelfEvaluationPrompt);
            ViewHelper.hideView(inflate.flActivitySelfEvaluationVideo);
            ViewHelper.hideView(inflate.btActivitySelfEvaluationDelete);
        } else {
            ViewHelper.showView(inflate.btActivitySelfEvaluationDelete);
            ViewHelper.hideView(inflate.flActivitySelfEvaluationPrompt);
            ViewHelper.showView(inflate.flActivitySelfEvaluationVideo);
            inflate.edActivitySelfEvaluation.setText(selfExperienceVm.getItem().getContent());

            if (!TextUtils.isEmpty(selfExperienceVm.getItem().getVideo())) {
                GlideHelper.loadVideImage(selfExperienceVm.getItem().getVideo(),inflate.ivActivitySelfEvaluationVideo);
//                inflate.ivActivitySelfEvaluationVideo.setImageBitmap(ResHelper.getCurrentVideoBitmap(selfExperienceVm.getItem().getVideo()));
            }
        }
    }

    private void initView() {
    }

    public void onClickAddVideo(View view) {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.MP4))
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
            this.path = path;

            GlideHelper.loadVideImage(safe,inflate.ivActivitySelfEvaluationVideo);

            if (TextUtils.isEmpty(path)) {
                ViewHelper.showView(inflate.flActivitySelfEvaluationPrompt);
                ViewHelper.hideView(inflate.flActivitySelfEvaluationVideo);
            } else {
                ViewHelper.hideView(inflate.flActivitySelfEvaluationPrompt);
                ViewHelper.showView(inflate.flActivitySelfEvaluationVideo);
            }
        }
    }

    public void onClickDelete(View view) {
        presenter.requestSubmit(view.getContext(),"", "");
    }

    public void onClickSubmit(View view) {
        presenter.requestSubmit(view.getContext(),path, inflate.edActivitySelfEvaluation.getText().toString());
    }

    public void onClickPlay(View view) {

    }

}
