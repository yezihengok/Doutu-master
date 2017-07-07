package com.yzi.doutu.interfaces;

import com.yzi.doutu.view.GradationScrollView;

/**
 * Created by yzh-t105 on 2017/7/6.
 */

public interface ScrollViewListener {
    void onScrollChanged(GradationScrollView scrollView, int x, int y,
                         int oldx, int oldy);
}
