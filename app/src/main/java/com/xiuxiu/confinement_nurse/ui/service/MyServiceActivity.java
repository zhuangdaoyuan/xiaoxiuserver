package com.xiuxiu.confinement_nurse.ui.service;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityMyServerBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.RouterHelper;
import com.xiuxiu.confinement_nurse.model.event.ServiceEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.SpacesItemDecoration;
import com.xiuxiu.confinement_nurse.ui.service.vm.MyServerInfoVm;
import com.xiuxiu.confinement_nurse.ui.service.adapter.MyServerAdapter;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

import static com.xiuxiu.confinement_nurse.help.RouterHelper.Constant.KEY_NEED_LOGIN;

/**
 * 我的服务
 */
@Route(path = RouterHelper.KEY_SERVICE, extras = KEY_NEED_LOGIN)
public class MyServiceActivity extends AbsBaseActivity implements MyServiceContract.IView {

    private MyServerAdapter myServerAdapter;
    private String ysId;
    public static void start(Context context) {
    start(context,"");
    }
    public static void start(Context context,String id) {
        ARouter.getInstance().build(RouterHelper.KEY_SERVICE)
                .withString("id",id)
                .navigation();
    }

    private ActivityMyServerBinding inflate;
    private MyServiceContract.IPresenter presenter;

    @Override
    protected View getLayoutView() {
        inflate = ActivityMyServerBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected Object getTargetView() {
        return inflate.content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initViewState();
        setListener();
        loadData();
    }

    private void loadData() {
        presenter.requestMyServiceListData();
    }

    private void setListener() {
        inflate.bvActivityServiceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditServiceActivity.start( null,ysId);
            }
        });
        myServerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

            }
        });

        myServerAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Object safe = CollectionUtil.getSafe(adapter.getData(), position, null);
                if (view.getId() == R.id.bv_layout_my_service_delete) {
                    if (safe instanceof MyServerInfoVm) {
                        presenter.requestDelete(position, (MyServerInfoVm) safe);
                    }
                } else {
                    if (safe instanceof MyServerInfoVm) {
                        EditServiceActivity.start((MyServerInfoVm) safe);
                    }
                }
            }
        });

        EventBus.AddService().

                observe(this, new Observer<ServiceEvent>() {
                    @Override
                    public void onChanged(ServiceEvent serviceEvent) {
                        presenter.requestMyServiceListData();
                    }
                });
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        onRequestLoading();
        presenter.requestMyServiceListData();
    }

    private void initViewState() {
        presenter = new MyServicePresenter(this);
        presenter.setYsId(ysId);
        this.myServerAdapter = new MyServerAdapter();
        inflate.rvMyServerList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        inflate.rvMyServerList.setAdapter(myServerAdapter);
        SpacesItemDecoration decor = new SpacesItemDecoration(ResHelper.pt2Px(46), ResHelper.pt2Px(46), ResHelper.pt2Px(58), ResHelper.pt2Px(29));
        decor.setLastBottom(ResHelper.pt2Px(175));
        inflate.rvMyServerList.addItemDecoration(decor);

        myServerAdapter.addChildClickViewIds(R.id.bv_layout_my_service_edit, R.id.bv_layout_my_service_delete);
    }

    private void initView() {
        ysId=getIntent().getStringExtra("id");
    }

    @Override
    public void onRequestMyServiceList(List<MyServerInfoVm> s) {
        myServerAdapter.setNewInstance(s);
    }

    @Override
    public void onRequestDeleteSuccess(int position) {
        myServerAdapter.removeAt(position);
        if (myServerAdapter.getData().size() == 0) {
            onRequestPageEmpty();
        }
    }
}
