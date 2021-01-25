package com.xiuxiu.confinement_nurse.ui.order.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseDelegateMultiAdapter;
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.ui.order.vm.FilterSwitchVm;
import com.xiuxiu.confinement_nurse.ui.order.vm.FilterTitleVm;
import com.xiuxiu.confinement_nurse.ui.order.vm.FilterVm;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilterAdapter extends BaseDelegateMultiAdapter<FilterVm, BaseViewHolder> {
    public static final int KEY_TITLE = 1;
    public static final int KEY_SWITCH = 2;

    public FilterAdapter() {
        super();
        // 绑定 layout 对应的 type
        // 第一步，设置代理
        setMultiTypeDelegate(new BaseMultiTypeDelegate<FilterVm>() {
            @Override
            public int getItemType(@NotNull List<? extends FilterVm> data, int position) {
                return data.get(position).getItemType();
            }
        });
        // 第二部，绑定 item 类型
        getMultiTypeDelegate()
                .addItemType(KEY_TITLE, R.layout.item_text_view)
                .addItemType(KEY_SWITCH, R.layout.item_switch_view);

    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, FilterVm filterVm) {
        // 根据返回的 type 分别设置数据
        switch (baseViewHolder.getItemViewType()) {
            case KEY_TITLE:
                setTitle(baseViewHolder, filterVm);
                break;
            case KEY_SWITCH:
                setSwitchRadio(baseViewHolder, filterVm);
                break;
            default:
                break;
        }
    }

    private void setTitle(BaseViewHolder baseViewHolder, FilterVm filterVm) {
        if (filterVm instanceof FilterTitleVm) {
            baseViewHolder.setText(R.id.item_text_view_title, ((FilterTitleVm) filterVm).getTitle());
            baseViewHolder.setText(R.id.item_text_view_sub_title, ((FilterTitleVm) filterVm).getSubtitle());
        }
    }

    private void setSwitchRadio(BaseViewHolder baseViewHolder, FilterVm filterVm) {
        if (filterVm instanceof FilterSwitchVm) {
            RecyclerView recyclerView = ((RecyclerView) baseViewHolder.itemView);
            recyclerView.setLayoutManager(new GridLayoutManager(baseViewHolder.itemView.getContext(), 3));

            FilterSwitchRadioAdapter filterSwitchRadioAdapter = new FilterSwitchRadioAdapter();
            recyclerView.setAdapter(filterSwitchRadioAdapter);
            filterSwitchRadioAdapter.setNewInstance(((FilterSwitchVm) filterVm).getList());
            filterSwitchRadioAdapter.notifyDataSetChanged();
        }
    }
}
