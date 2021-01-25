package com.xiuxiu.confinement_nurse.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.haibin.calendarview.Calendar;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityMyScheduleBinding;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.SpacesItemDecoration;
import com.xiuxiu.confinement_nurse.ui.dialog.CalendarDialog;
import com.xiuxiu.confinement_nurse.ui.office.OfficeItemPresenter;
import com.xiuxiu.confinement_nurse.ui.office.adapter.OfficeItemAdapter;
import com.xiuxiu.confinement_nurse.ui.schedule.adapter.ScheduleItemAdapter;
import com.xiuxiu.confinement_nurse.ui.schedule.vm.ScheduleVm;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.List;

/**
 * 月嫂下的档期
 */
public class MyScheduleActivity extends AbsBaseActivity implements MyScheduleContract.IView {
    private ScheduleItemAdapter scheduleItemAdapter;
    private ActivityMyScheduleBinding inflate;
    private MyScheduleContract.IPresenter presenter;
    private String id;
    private String name;

    public static void start(Context context) {
        Intent starter = new Intent(context, MyScheduleActivity.class);
        context.startActivity(starter);
    }

    public static void start(Context context, String id, String name) {
        Intent starter = new Intent(context, MyScheduleActivity.class);
        starter.putExtra("id", id);
        starter.putExtra("name", name);
        context.startActivity(starter);
    }


    @Override
    protected Object getTargetView() {
        return inflate.recyclerView;
    }

    @Override
    protected View getLayoutView() {
        inflate = ActivityMyScheduleBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        scheduleItemAdapter.setOnItemChildClickListener((adapter, view, position) -> onClickScheduleView(adapter, position));
        scheduleItemAdapter.setOnItemClickListener((adapter, view, position) -> onClickScheduleView(adapter, position));
    }

    private void onClickScheduleView(@NonNull BaseQuickAdapter<?, ?> adapter, int position) {
        Object safe = CollectionUtil.getSafe(adapter.getData(), position, null);
        if (safe instanceof ScheduleVm) {
            ScheduleVm safe1 = (ScheduleVm) safe;
            DialogHelper.showCalendarDialog(MyScheduleActivity.this, safe1, new CalendarDialog.ISelectCalendar() {
                @Override
                public void selectCalendar(List<Calendar> multiSelectCalendars) {
                    presenter.requestSubmit(safe1, multiSelectCalendars);
                }
            });
        }
    }

    private void initViewState() {
        presenter = new MySchedulePresenter(this);
        presenter.setId(id);
        scheduleItemAdapter = new ScheduleItemAdapter(new XFunc1<Integer>() {
            @Override
            public void call(Integer integer) {
                onClickScheduleView(scheduleItemAdapter, integer);
            }
        });
        inflate.recyclerView.setAdapter(scheduleItemAdapter);
        inflate.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        inflate.recyclerView.addItemDecoration(new SpacesItemDecoration(0, 0,
                ResHelper.pt2Px(124), 0));
        scheduleItemAdapter.addChildClickViewIds(R.id.calendarView);
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(id)) {
            inflate.activityMyScheduleTitle.setText((TextUtils.isEmpty(name) ? "月嫂" : name) + "的档期");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestListData(List<ScheduleVm> s) {
        scheduleItemAdapter.setNewInstance(s);
    }

    @Override
    public void onRequestSubmitSuccess() {
        loadData();


    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        onRequestLoading();
        loadData();
    }
}
