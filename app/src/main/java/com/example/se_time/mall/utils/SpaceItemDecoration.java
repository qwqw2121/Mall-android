package com.example.se_time.mall.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int bottomSpace;
    private int outSpace;

    public SpaceItemDecoration(int bottomSpace, int outSpace) {
        this.bottomSpace = bottomSpace;
        this.outSpace = outSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom=bottomSpace;
        if(parent.getChildLayoutPosition(view)%2==0)
        {
            outRect.right=outSpace;
        }else {
            outRect.left=outSpace;
        }
    }
}
