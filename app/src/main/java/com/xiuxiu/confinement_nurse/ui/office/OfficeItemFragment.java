package com.xiuxiu.confinement_nurse.ui.office;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.monster.gamma.callback.LayoutEmpty;
import com.monster.gamma.callback.LayoutError;
import com.monster.gamma.callback.LayoutNoLogin;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.FragmentOrderItemBinding;
import com.xiuxiu.confinement_nurse.help.DialogHelper;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.event.FilterEvent;
import com.xiuxiu.confinement_nurse.model.event.LoginEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseFragment;
import com.xiuxiu.confinement_nurse.ui.base.SpacesItemDecoration;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;
import com.xiuxiu.confinement_nurse.ui.login.LoginActivity;
import com.xiuxiu.confinement_nurse.ui.office.adapter.OfficeItemAdapter;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeBean;
import com.xiuxiu.confinement_nurse.ui.office.vm.OfficeItemVm;
import com.xiuxiu.confinement_nurse.ui.vm.LocationVm;
import com.xiuxiu.confinement_nurse.utlis.CollectionUtil;

import java.util.List;

/**
 * 首页职位推荐，最近，附近
 */
public class OfficeItemFragment extends AbsBaseFragment implements OfficeItemContract.IView {


    public static OfficeItemFragment newInstance(String type) {
        Bundle args = new Bundle();
        OfficeItemFragment fragment = new OfficeItemFragment();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    private LocationVm locationVm;
    private FragmentOrderItemBinding mViewBinding;
    private OfficeItemAdapter orderRecommendAdapter;
    private OfficeItemContract.IPresenter presenter;

    private String type;

    private boolean isLocation;

    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        mViewBinding = FragmentOrderItemBinding.inflate(inflater, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData(savedInstanceState);
        initViewState();
        setListener();
        loadData();
    }


    private void loadData() {
        if (TextUtils.isEmpty(type)) {
            onRequestPageNetError();
            return;
        }
        if (!UserHelper.isLogin()) {
            loadService.showCallback(LayoutNoLogin.class);
        } else {
            if (isLocation) {
                presenter.requestOfficeData(type);
            }
        }
    }

    private void setListener() {
        locationVm.getCityLiveData().observe(this, new Observer<City>() {
            @Override
            public void onChanged(City city) {
                isLocation = true;
                loadData();
            }
        });
        EventBus.FilterChange()
                .observe(this, new Observer<FilterEvent>() {
                    @Override
                    public void onChanged(FilterEvent filterEvent) {
                        loadData();
                    }
                });
        EventBus.LoginEvent()
                .observe(this, loginEvent ->{
                    if( UserHelper.isYuesao()){
                        loadData();
                    }

                });
        EventBus.LogoutEvent()
                .observe(this, new Observer<LoginEvent>() {
                    @Override
                    public void onChanged(LoginEvent loginEvent) {
                        loadData();
                    }
                });
        orderRecommendAdapter.getLoadMoreModule().setEnableLoadMore(true);
        orderRecommendAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                presenter.onLoadMore();
            }
        });
        orderRecommendAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Object safe = CollectionUtil.getSafe(adapter.getData(), position, null);
                if (safe instanceof OfficeBean) {
                    String orderId = ((OfficeBean) safe).getEmploymentId();
                    OfficeDetailsActivity.start(view.getContext(), orderId);
                }
            }
        });

        orderRecommendAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.tv_item_order_recommend_delivery) {
                    Object safe = CollectionUtil.getSafe(adapter.getData(), position, null);
                    if (safe instanceof OfficeBean) {
                        String deliveryState = ((OfficeBean) safe).getDeliveryState();
                        if (!TextUtils.isEmpty(deliveryState)) {
                            if (TextUtils.equals(deliveryState, "0")) {
                                presenter.requestDelivery(position, ((OfficeBean) safe).getEmploymentId());
                            }
                        }

                    }
                }
            }
        });
        mViewBinding.srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.requestOfficeData(type);
            }
        });
    }

    private void initViewState() {
        locationVm = ViewModelProviders.of(getActivity()).get(LocationVm.class);

        presenter = new OfficeItemPresenter(this);
        orderRecommendAdapter = new OfficeItemAdapter();
        mViewBinding.rvNewDetail.setAdapter(orderRecommendAdapter);
        mViewBinding.rvNewDetail.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mViewBinding.rvNewDetail.addItemDecoration(new SpacesItemDecoration(ResHelper.pt2Px(46), ResHelper.pt2Px(46),
                ResHelper.pt2Px(24), ResHelper.pt2Px(102)));
        orderRecommendAdapter.addChildClickViewIds(R.id.tv_item_order_recommend_delivery);

    }

    private void initView(View view) {
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            type = savedInstanceState.getString("type");
        } else {
            if (getArguments() != null) {
                type = getArguments().getString("type");
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", type);
    }

    @Override
    public void onRequestOfficeData(List<OfficeBean> orderTitleVms) {
        orderRecommendAdapter.setNewInstance(orderTitleVms);
    }

    @Override
    public void onRequestRefreshing(boolean b) {
        mViewBinding.srl.setRefreshing(b);
    }

    @Override
    public void onRequestOfficePageData(List<OfficeBean> officeBeans) {
        orderRecommendAdapter.addData(officeBeans);
        orderRecommendAdapter.getLoadMoreModule().loadMoreEnd();
    }

    @Override
    public void onRequestOfficePageDataError() {
        mViewBinding.srl.setRefreshing(false);
        orderRecommendAdapter.getLoadMoreModule().setEnableLoadMore(true);
        orderRecommendAdapter.getLoadMoreModule().loadMoreFail();
    }

    @Override
    public void onRequestDelivery(int position) {
        OfficeBean safe = CollectionUtil.getSafe(orderRecommendAdapter.getData(), position, null);
        safe.setDeliveryState("2");
        orderRecommendAdapter.notifyItemChanged(position);
        DialogHelper.showSuccessfulDeliveryDialog(getContext());
    }

    @Override
    public void onRequestOfficeComplete() {
        orderRecommendAdapter.getLoadMoreModule().loadMoreComplete();
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        Class currentCallback = loadService.getCurrentCallback();
        if (currentCallback == LayoutNoLogin.class) {
            LoginActivity.start(getActivity());
        } else if (currentCallback == LayoutEmpty.class) {
            loadData();
        } else if (currentCallback == LayoutError.class) {
            loadData();
        }
    }

}
