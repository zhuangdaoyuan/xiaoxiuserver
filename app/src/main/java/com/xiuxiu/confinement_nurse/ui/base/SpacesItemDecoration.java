package com.xiuxiu.confinement_nurse.ui.base;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int left;
    private int right;
    private int bottom;
    private int top;

    private int lastBottom;

    public SpacesItemDecoration(int space) {
        this.left = space;
        this.right = space;
        this.bottom = space;
        this.top = space;
    }

    public SpacesItemDecoration(int left, int right, int bottom, int top) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
    }

    public void setLastBottom(int lastBottom) {
        this.lastBottom = lastBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;

        // Add top margin only for the first item to avoid double space between items
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        if (childLayoutPosition == 0) {
            outRect.top = top;
        } else {
            if (lastBottom != 0 && parent.getAdapter() != null) {
                int itemCount = parent.getAdapter().getItemCount();
                if (childLayoutPosition == itemCount - 1) {
                    outRect.bottom = lastBottom;
                }
            }
        }

    }
}
