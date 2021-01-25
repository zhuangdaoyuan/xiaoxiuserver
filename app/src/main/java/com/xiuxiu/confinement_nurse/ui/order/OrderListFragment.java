package com.xiuxiu.confinement_nurse.ui.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.monster.gamma.callback.GammaCallback;
import com.monster.gamma.callback.LayoutNoOrder;
import com.xiuxiu.confinement_nurse.EventBus;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.databinding.FragmentOrderListBinding;
import com.xiuxiu.confinement_nurse.help.ResHelper;
import com.xiuxiu.confinement_nurse.model.event.OrderEvent;
import com.xiuxiu.confinement_nurse.ui.base.AbsBaseFragment;
import com.xiuxiu.confinement_nurse.ui.base.BaseActivity;
import com.xiuxiu.confinement_nurse.ui.base.BaseFragment;
import com.xiuxiu.confinement_nurse.ui.base.SpacesItemDecoration;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderBean;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderInfoViewModel;
import com.xiuxiu.confinement_nurse.ui.order.vm.OrderListItemVm;
import com.xiuxiu.confinement_nurse.ui.order.adapter.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表
 */
public class OrderListFragment extends BaseFragment implements GammaCallback.OnReloadListener, OrderListContract.IView {

    private OrderListAdapter orderRecommendAdapter;
    private String type;
    private OrderListContract.IPresenter presenter;
    private OrderInfoViewModel orderInfoViewModel;
    public static OrderListFragment newInstance(String tag) {
        Bundle args = new Bundle();
        OrderListFragment fragment = new OrderListFragment();
        args.putString("type", tag);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentOrderListBinding mViewBinding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = FragmentOrderListBinding.inflate(inflater, container, false);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", type);
    }

    private void loadData() {
        if (!TextUtils.isEmpty(type)) {
            presenter.requestOrderData(type);
        }
    }

    @Override
    public void onRequestPageError(int code) {

    }

    @Override
    public void onRequestPageNetError() {

    }

    @Override
    public void onRequestPageSuccess() {

    }

    @Override
    public void onRequestPageEmpty() {
        orderRecommendAdapter.setNewInstance(new ArrayList<>());
    }

    @Override
    public void onRequestLoading() {

    }

    @Override
    public void onRequestFinish() {

    }

    private void setListener() {
        orderRecommendAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Object o = adapter.getData().get(position);
                if (o instanceof OrderBean) {
                    OrderListDetailsActivity.start(view.getContext(), ((OrderBean) o).getOrderId(),orderInfoViewModel.getMatronId());
                }
            }
        });

        orderRecommendAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Object o = adapter.getData().get(position);
                if (o instanceof OrderBean) {
                    if (view.getId() == R.id.tv_layout_orderlist_reject) {
                        presenter.requestRefuse((OrderBean) o);
                    } else if (view.getId() == R.id.tv_layout_orderlist_confirm) {
                        presenter.requestOperateOrder((OrderBean) o);
                    }
                }
            }
        });
        mViewBinding.srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        EventBus.OrderEvent().observe(this, new Observer<OrderEvent>() {
            @Override
            public void onChanged(OrderEvent orderEvent) {
                loadData();
            }
        });
    }


    private void initViewState() {
        presenter = new OrderListPresenter(this);
        presenter.requestYsId(orderInfoViewModel.getMatronId());
        presenter.setJiGoud(orderInfoViewModel.isJiGou());
        orderRecommendAdapter = new OrderListAdapter();
        mViewBinding.rvNewDetail.setAdapter(orderRecommendAdapter);
        mViewBinding.rvNewDetail.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mViewBinding.rvNewDetail.addItemDecoration(new SpacesItemDecoration(ResHelper.pt2Px(46), ResHelper.pt2Px(46),
                ResHelper.pt2Px(24), 20));
        orderRecommendAdapter.addChildClickViewIds(R.id.tv_layout_orderlist_confirm, R.id.tv_layout_orderlist_reject);

        orderRecommendAdapter.setEmptyView(R.layout.layout_no_order);
        orderRecommendAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                presenter.requestLoadMore();
            }
        });
    }

    private void initView(View view) {
        orderInfoViewModel= ViewModelProviders.of(getActivity()).get(OrderInfoViewModel.class);
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (getArguments() != null) {
                type = getArguments().getString("type");
            }
        } else {
            type = savedInstanceState.getString("type");
        }
    }


    @Override
    public void onReload(View v) {

    }

    @Override
    public void onRequestDataList(List<OrderBean> items) {
        orderRecommendAdapter.setNewInstance(items);
    }

    @Override
    public void onRequestRefuseSuccess() {
        loadData();
    }

    @Override
    public void onRequestConfirmSuccess() {
        loadData();
    }

    @Override
    public void onRequestOrderComplete() {
        orderRecommendAdapter.getLoadMoreModule().loadMoreComplete();
    }

    @Override
    public void onRequestOrderError() {
        orderRecommendAdapter.getLoadMoreModule().loadMoreFail();
    }

    @Override
    public void onRequestRefreshing(boolean b) {
        mViewBinding.srl.setRefreshing(b);
    }

    @Override
    public void onRequestNoPage() {
        orderRecommendAdapter.getLoadMoreModule().loadMoreEnd();
    }
    private BasePopupView basePopupView;
    @Override
    public void showLoadingDialog() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    basePopupView = new XPopup.Builder(getContext())
                            .asLoading("正在加载中")
                            .show();
                }
            });
        }
    }

    @Override
    public void cancelLoadingDialog() {
        basePopupView.post(new Runnable() {
            @Override
            public void run() {
                if (basePopupView != null) {
                    basePopupView.dismiss();
                }
            }
        });

    }
}
