package com.yzi.doutu.utils.RecycleViews;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * GridLayoutManager 使用的间距
 * Created by yzh-t105 on 2016/9/29.
 */

public class SpaceItemDecorations extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecorations(int space) {
        this.space = space;
    }


    //间距只有中间的格子和底部的格式之间有
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) %3==0) {
            outRect.left = 0;
        }
    }

}
