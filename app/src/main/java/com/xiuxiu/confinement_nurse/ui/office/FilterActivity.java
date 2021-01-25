package com.xiuxiu.confinement_nurse.ui.office;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.ActivityFilterBinding;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.event.FilterEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseActivity;
import com.xiuxiu.confinement_nurse.ui.office.vm.FilterRadioVm;
import com.xiuxiu.confinement_nurse.ui.order.adapter.FilterSwitchRadioAdapter;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;

import java.util.List;

public class FilterActivity extends AbsBaseActivity implements FilterContract.IView {

    public static void start(Activity context) {
        Intent starter = new Intent(context, FilterActivity.class);
        context.startActivityForResult(starter, 1);
    }

    private FilterContract.IPresenter presenter;
    private ActivityFilterBinding inflate;
    private FilterSwitchRadioAdapter filterResumeAdapter;
    private FilterSwitchRadioAdapter filterIntervalAdapter;
    private FilterSwitchRadioAdapter filterTimeAdapter;

    @Override
    protected View getLayoutView() {
        inflate = ActivityFilterBinding.inflate(LayoutInflater.from(this));
        return inflate.getRoot();
    }

    @Override
    protected Object getTargetView() {
        return inflate.recyclerView;
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
        int dayRangeType = ModelManager.getInstance().getCacheInterface().getDayRangeType();
        int priceRangeType = ModelManager.getInstance().getCacheInterface().getPriceRangeType();
        int deliveryType = ModelManager.getInstance().getCacheInterface().getDeliveryType();
        presenter.requestFilterData(deliveryType, priceRangeType, dayRangeType);
    }

    private void setListener() {
        inflate.btActivityFilterDefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mDeliveryType = -1;
                int mPriceRangeType = -1;
                int mDayRangeType = -1;
                List<FilterRadioVm> data = filterResumeAdapter.getData();
                for (FilterRadioVm filterRadioVm : data) {
                    if (filterRadioVm.getState() == 1) {
                        mDeliveryType = filterRadioVm.getCode();
                        break;
                    }
                }
                data = filterIntervalAdapter.getData();
                for (FilterRadioVm filterRadioVm : data) {
                    if (filterRadioVm.getState() == 1) {
                        mPriceRangeType = filterRadioVm.getCode();
                        break;
                    }
                }
                data = filterTimeAdapter.getData();
                for (FilterRadioVm filterRadioVm : data) {
                    if (filterRadioVm.getState() == 1) {
                        mDayRangeType = filterRadioVm.getCode();
                        break;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("mDeliveryType", mDeliveryType);
                intent.putExtra("mPriceRangeType", mPriceRangeType);
                intent.putExtra("mDayRangeType", mDayRangeType);

                ModelManager.getInstance().getCacheInterface().saveDeliveryType(mDayRangeType);
                ModelManager.getInstance().getCacheInterface().savePriceRangeType(mPriceRangeType);
                ModelManager.getInstance().getCacheInterface().saveDayRangeType(mDayRangeType);

                EventBus.FilterChange().post(new FilterEvent());
                setResult(1, intent);
                finish();
            }
        });
        inflate.btActivityFilterClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearItemState(filterResumeAdapter);
                clearItemState(filterIntervalAdapter);
                clearItemState(filterTimeAdapter);
                ModelManager.getInstance().getCacheInterface().saveDeliveryType(-1);
                ModelManager.getInstance().getCacheInterface().savePriceRangeType(-1);
                ModelManager.getInstance().getCacheInterface().saveDayRangeType(-1);
                loadData();
            }
        });
        filterResumeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                changeResumeViewState(position, filterResumeAdapter);
            }
        });

        filterIntervalAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                changeResumeViewState(position, filterIntervalAdapter);
            }
        });

        filterTimeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
//                FilterRadioVm safe = CollectionUtil.getSafe(filterTimeAdapter.getData(), position, null);
//                if (safe != null) {
//                    int state = safe.getState();
//                    safe.setState(state == 1 ? 0 : 1);
//                    filterTimeAdapter.notifyItemChanged(position);
//                }
                changeResumeViewState(position, filterTimeAdapter);
            }
        });
    }

    private void changeResumeViewState(int position, FilterSwitchRadioAdapter filterResumeAdapter) {
        FilterRadioVm safe = CollectionUtil.getSafe(filterResumeAdapter.getData(), position, null);
        if (safe != null) {
            int state = safe.getState();
            clearItemState(filterResumeAdapter);
            safe.setState(state == 1 ? 0 : 1);
            filterResumeAdapter.notifyItemChanged(position);
        }
    }

    private void clearItemState(FilterSwitchRadioAdapter filterResumeAdapter) {
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

    private void initViewState() {
        presenter = new FilterPresenter(this);
        inflate.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        filterResumeAdapter = new FilterSwitchRadioAdapter();
        filterResumeAdapter.setRadio(true);
        inflate.recyclerView.setAdapter(filterResumeAdapter);
        filterResumeAdapter.addChildClickViewIds(R.id.item_switch_radio_sb);


        inflate.recyclerView1.setLayoutManager(new GridLayoutManager(this, 3));
        filterIntervalAdapter = new FilterSwitchRadioAdapter();
        filterIntervalAdapter.setRadio(true);
        inflate.recyclerView1.setAdapter(filterIntervalAdapter);
        filterIntervalAdapter.addChildClickViewIds(R.id.item_switch_radio_sb);

        inflate.recyclerView2.setLayoutManager(new GridLayoutManager(this, 3));
        filterTimeAdapter = new FilterSwitchRadioAdapter();
        inflate.recyclerView2.setAdapter(filterTimeAdapter);

        filterTimeAdapter.addChildClickViewIds(R.id.item_switch_radio_sb);

    }

    private void initView() {
    }


    @Override
    public void onRequestResume(List<FilterRadioVm> resumeItem) {
        filterResumeAdapter.setNewInstance(resumeItem);
        filterResumeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRequestinterval(List<FilterRadioVm> intervalItem) {
        filterIntervalAdapter.setNewInstance(intervalItem);
        filterIntervalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestTime(List<FilterRadioVm> timeItem) {
        filterTimeAdapter.setNewInstance(timeItem);
        filterTimeAdapter.notifyDataSetChanged();
    }
}
