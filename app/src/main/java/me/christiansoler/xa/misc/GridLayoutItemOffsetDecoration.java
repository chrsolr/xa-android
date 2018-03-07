package me.christiansoler.xa.misc;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridLayoutItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;

    public GridLayoutItemOffsetDecoration(int itemOffset) {
        this.mItemOffset = itemOffset;
    }

    public GridLayoutItemOffsetDecoration(Context context, int itemOffset) {
        this(itemOffset);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
